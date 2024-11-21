import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {ApiService} from '../services/api.service';

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

  constructor(private fb: FormBuilder, private apiService: ApiService) {
    this.preferencesForm = this.fb.group({
      transportation: this.fb.group({
        onFoot: false,
        byBike: false,
        byCar: false,
        byPublicTransport: false
      }),
      address: this.fb.group({
        street: '',
        city: '',
        zip: '',
        country: ''
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
      this.preferencesForm.setValue(JSON.parse(savedPreferences));
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

    this.apiService.setAlarmPreference(alarmId, alarmValue).subscribe(() => {
      console.log("Wecker gestellt");
    })

    // set allergene
    const allergens = Object.keys(formData.allergens).filter(key => formData.allergens[key]);
    console.log(allergens)
    this.apiService.setAllergenePreference(allergens).subscribe(() => {
      console.log("Allergene gesetzt");
    })
  }
}
