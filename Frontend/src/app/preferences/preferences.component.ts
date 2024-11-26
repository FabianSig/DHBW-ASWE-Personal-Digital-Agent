import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {ApiService} from '../services/api.service';
import { TriggerService } from '../services/trigger.service';
import {forkJoin, of, tap} from 'rxjs';
import { format } from 'date-fns';

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
      }),
      stocks: this.fb.group({
        stock1: '',
        stock2: '',
        stock3: ''
      }),
      news: this.fb.group({
        topic: ''
      }),
      contacts: this.fb.group({
        contact1: '',
        contact2: '',
        contact3: '',
      })
    });
  }

  ngOnInit() {
    this.loadPreferences()
  }

  onSubmit() {
    const formData = this.preferencesForm.value;
    console.log('Form submitted with data:', formData);

    const preferenceMapping = [
      { id: 'travelMode', path: 'transportation.mode' },
      { id: 'home', path: 'homeAddress.address' },
      { id: 'work', path: 'workAddress.address' },
      { id: 'news-topic', path: 'news.topic' },
      {
        id: 'allergene',
        path: 'allergens',
        process: (value: any) => Object.entries(value)
          .filter(([_, v]) => v) // Get only true allergens
          .map(([key]) => key)   // Extract keys
      },
      {
        id: 'korb',
        path: 'email',
        process: (value: any) => [value.korb1, value.korb2, value.korb3]
      },
      {
        id: 'stock',
        path: 'stocks',
        process: (value: any) => [value.stock1, value.stock2, value.stock3]
      },
      {
        id: 'contact',
        path: 'contacts',
        process: (value: any) => [value.contact1, value.contact2, value.contact3]
      },
      {
        id: 'alarm',
        path: 'alarm',
        process: (value: any) => {
          const { alarmDate, alarmTime } = value;
          if (alarmDate && alarmTime) {
            const alarmDateTime = new Date(`${alarmDate}T${alarmTime}`);
            const formattedLocalTime = format(alarmDateTime, "yyyy-MM-dd'T'HH:mm:ss");
            return [formattedLocalTime];
          }
          return [];
        }
      }

    ];

    const updateObservables = preferenceMapping.map(({ id, path, process }) => {
      const controlValue = this.getNestedValue(formData, path);
      if (controlValue !== undefined && controlValue !== null) {
        const processedValue = process ? process(controlValue) : [controlValue];
        return this.apiService.setPreferences(id, processedValue).pipe(
          tap(() => {
            console.log(`Preference ${id} updated:`, processedValue);
          })
        );
      } else {
        return of(null);
      }
    });

    forkJoin(updateObservables).subscribe({
      next: () => {
        console.log('All preferences updated.');
        this.triggerService.reload();
      },
      error: (err) => {
        console.error('An error occurred while updating preferences:', err);
      },
    });

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
      { id: 'korb', formControlPath: 'email' },
      { id: 'stock', formControlPath: 'stocks' },
      { id: 'news-topic', formControlPath: 'news.topic'},
      { id: 'contact', formControlPath: 'contacts'}
    ];

    // Fetch each preference and update the form
    preferenceIds.forEach((pref) => {
      this.apiService.getPreference(pref.id).subscribe((response) => {
        if (response && response.value) {
          const control = this.preferencesForm.get(pref.formControlPath);

          if (!control) {
            console.warn(`Control not found for path: ${pref.formControlPath}`);
            return;
          }

          if (pref.id === 'allergene') {
            // Update allergens dynamically
            const allergens = response.value.reduce((acc: Record<string, boolean>, allergen: string) => {
              acc[allergen] = true;
              return acc;
            }, {});
            control.patchValue(allergens);
          } else if (pref.id === 'korb' || pref.id === 'stock' || pref.id === 'contact') {
            // Update email preferences dynamically
            response.value.forEach((value: string, index: number) => {
              const key = `${pref.id}${index + 1}`;
              const emailControl = control.get(key);
              if (emailControl) {
                emailControl.patchValue(value);
              }
            });
          }  else {
            // Update single values
            control.patchValue(response.value[0]);
          }
          console.log(`Preference ${pref.id} loaded and updated.`);
        }
      });
    });
  }

  private getNestedValue(obj: any, path: string) {
    return path.split('.').reduce((acc, key) => acc && acc[key], obj);
  }
}
