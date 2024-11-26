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
    console.log("ngOnInit")

    this.apiService.getPreferenceFormFromDB().subscribe((form: any) => {
      console.log("form:", form);

      if (form) {
        console.log('Loaded Preferences:', form);

        if (!form.transportation?.mode) {
          form.transportation = { ...form.transportation, mode: 'transit' };
        }

        this.preferencesForm.setValue(form);
      }
    });
  }

  onSubmit() {
    const formData = this.preferencesForm.value;
    console.log("formData: " + JSON.stringify(formData))
    this.apiService.setPreferenceFormInDB(formData).subscribe(() => {})
  }
}
