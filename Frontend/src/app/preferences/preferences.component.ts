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
      email: this.fb.group({
        korb1: '',
        korb2: '',
        korb3: ''
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
    this.loadPreferences()
  }

  onSubmit() {
    const formData = this.preferencesForm.value;
    console.log('Form submitted with data:', formData);

    // Update transportation mode
    const transportationMode = formData.transportation.mode;
    this.apiService.setPreferences('travelMode', [transportationMode]).subscribe();
    console.log('Transportation mode updated:', transportationMode);

    // Update home address
    const homeAddress = formData.homeAddress.address;
    this.apiService.setPreferences('home', [homeAddress]).subscribe();
    console.log('Home address updated:', homeAddress);

    // Update work address
    const workAddress = formData.workAddress.address;
    this.apiService.setPreferences('work', [workAddress]).subscribe();
    console.log('Work address updated:', workAddress);

    // Update allergens
    const allergens = Object.entries(formData.allergens)
      .filter(([key, value]) => value) // Get only allergens with `true` values
      .map(([key]) => key); // Extract allergen keys
    this.apiService.setPreferences('allergene', allergens).subscribe();
    console.log('Allergens updated:', allergens);

    // // Update reminder
    // const reminder = formData.reminder;
    // this.apiService.setPreferences('reminder', [reminder]);
    // console.log('Reminder updated:', reminder);

    // Update alarm
    const alarmId = `alarm-${formData.alarm.alarmDate}`;
    const alarmValue = `${formData.alarm.alarmDate}T${formData.alarm.alarmTime}:00`;
    this.apiService.setPreferences(alarmId, [alarmValue]).subscribe();
    console.log('Alarm updated:', { alarmId, alarmValue });

    console.log('Preferences update completed.');
  }

  private loadPreferences() {
    // Define the preference IDs
    const preferenceIds = [
      { id: 'travelMode', formControlPath: 'transportation.mode' },
      { id: 'home', formControlPath: 'homeAddress.address' },
      { id: 'work', formControlPath: 'workAddress.address' },
      { id: 'allergene', formControlPath: 'allergens' },
      { id: 'reminder', formControlPath: 'reminder' },
    ];

    // Fetch each preference and update the form
    preferenceIds.forEach((pref) => {
      this.apiService.getPreference(pref.id).subscribe((response) => {
        if (response && response.value) {
          if (pref.id === 'allergene') {
            // Update allergens (special case for multiple values)
            const allergens = response.value.reduce((acc: Record<string, boolean>, allergen: string) => {
              acc[allergen] = true;
              return acc;
            }, {} as Record<string, boolean>);
            this.preferencesForm.get(pref.formControlPath)?.patchValue(allergens);
          } else {
            // Update single values
            const control = this.preferencesForm.get(pref.formControlPath);
            if (control) {
              control.patchValue(response.value[0]);
            }
          }
          console.log(`Preference ${pref.id} loaded and updated.`);
        }
      });
    });
  }

}
