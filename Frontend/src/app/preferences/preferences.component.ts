import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-preferences',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './preferences.component.html',
  styleUrl: './preferences.component.scss'
})
export class PreferencesComponent {
  preferencesForm: FormGroup;
  preferencesJson = '';

  constructor(private fb: FormBuilder) {
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
      reminder: ''
    });
  }

  onSubmit() {
    const formData = this.preferencesForm.value;
    this.preferencesJson = JSON.stringify(formData);
  }
}
