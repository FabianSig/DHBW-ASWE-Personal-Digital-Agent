import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {ApiService} from '../services/api.service';
import { TriggerService } from '../services/trigger.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-preferences',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './preferences.component.html',
  styleUrl: './preferences.component.scss'
})
export class PreferencesComponent implements OnInit {
  preferencesForm: FormGroup;
  preferencesJson = '';

  constructor(private fb: FormBuilder, private apiService: ApiService, private triggerService: TriggerService) {
    this.preferencesForm = this.fb.group({
      transportation: this.fb.group({
        mode: 'transit'
      }),
      homeAddress: this.fb.group({
        address: ''
      }),
      workAddress: this.fb.group({
        address: ''
      }),
      reminder: '',
      alarm: this.fb.group({
        alarmDate: '',
        alarmTime: ''
      }),
      allergens: this.fb.group({
        Ei: false,
        En: false,
        Fi: false,
        GID: false,
        GIG: false,
        GIH: false,
        GIKW: false,
        GIR: false,
        GIW: false,
        Kr: false,
        La: false,
        Lu: false,
        NuC: false,
        NuH: false,
        NuM: false,
        NuMa: false,
        NuPa: false,
        NuPe: false,
        NuPi: false,
        NuW: false,
        Se: false,
        Sf: false,
        Sl: false,
        So: false,
        Sw: false,
        Wt: false
      })
    });
  }

  ngOnInit() {
    // Load existing preferences from localStorag
    const savedPreferences = localStorage.getItem('preferences');
    if (savedPreferences) {
      const parsedPreferences = JSON.parse(savedPreferences);
      console.log('Loaded Preferences:', parsedPreferences);
      if (!parsedPreferences.transportation?.mode) {
        parsedPreferences.transportation = { ...parsedPreferences.transportation, mode: 'transit' };
      }
      this.preferencesForm.setValue(parsedPreferences);
    }
  }

  onSubmit() {
    const formData = this.preferencesForm.value;
    this.preferencesJson = JSON.stringify(formData);

    // Save the preferences in localStorage
    localStorage.setItem('preferences', this.preferencesJson);

    // set alarm
    const alarmId = "wecker-" + formData.alarm.alarmDate;
    const alarmValue = formData.alarm.alarmDate + "T" + formData.alarm.alarmTime + ":00"  ;
    const alarm$ = this.apiService.setAlarmPreference(alarmId, alarmValue);

    const travelMode$ = this.apiService.setTravelModePreference(formData.transportation.mode);

    const allergens = Object.keys(formData.allergens).filter(key => formData.allergens[key]);
    const allergens$ = this.apiService.setAllergenePreference(allergens);

    const workAddress$ = this.apiService.setWorkAddress(formData.workAddress.address)

    const homeAddress$ = this.apiService.setHomeAddress(formData.homeAddress.address)

    //Wir machen hier mit RxJS einen forkjoin, damit wir sicherstellen, dass alle preferences gesezt worden sind bevor wir reloaded
    forkJoin([alarm$, travelMode$, allergens$, workAddress$, homeAddress$]).subscribe({
      next: ([alarmResult, travelModeResult, allergensResult]) => {
        console.log("All API calls completed.");
        this.triggerService.reload();
      },
      error: (err) => {
        console.error("Error during API calls:", err);
      }
    });


  }
}
