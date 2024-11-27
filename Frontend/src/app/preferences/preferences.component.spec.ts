import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { PreferencesComponent } from './preferences.component';
import { ApiService } from '../services/api.service';
import { TriggerService } from '../services/trigger.service';
import { of } from 'rxjs';

class MockApiService {
  setPreferences = jasmine.createSpy('setPreferences').and.returnValue(of(null));
  getPreference = jasmine.createSpy('getPreference').and.returnValue(of({ value: ['test'] }));
}

class MockTriggerService {}

describe('PreferencesComponent', () => {
  let component: PreferencesComponent;
  let fixture: ComponentFixture<PreferencesComponent>;
  let apiService: ApiService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [PreferencesComponent],
      providers: [
        FormBuilder,
        { provide: ApiService, useClass: MockApiService },
        { provide: TriggerService, useClass: MockTriggerService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PreferencesComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService);
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load preferences on init', () => {
    component.ngOnInit();
    expect(apiService.getPreference).toHaveBeenCalled();
  });

  it('should submit the form and call apiService methods', () => {
    component.preferencesForm.setValue({
      transportation: { mode: 'car' },
      homeAddress: { address: 'Home Address' },
      workAddress: { address: 'Work Address' },
      email: { korb1: '', korb2: '', korb3: '' },
      reminder: '',
      alarm: { alarmDate: '2024-01-01', alarmTime: '08:00' },
      allergens: { Ei: true, En: false, Fi: false, GID: false, GIG: false, GIH: false, GIKW: false, GIR: false, GIW: false, Kr: false, La: false, Lu: false, NuC: false, NuH: false, NuM: false, NuMa: false, NuPa: false, NuPe: false, NuPi: false, NuW: false, Se: false, Sf: false, Sl: false, So: false, Sw: false, Wt: false },
    });

    component.onSubmit();

    expect(apiService.setPreferences).toHaveBeenCalledWith('travelMode', ['car']);
    expect(apiService.setPreferences).toHaveBeenCalledWith('home', ['Home Address']);
    expect(apiService.setPreferences).toHaveBeenCalledWith('work', ['Work Address']);
    expect(apiService.setPreferences).toHaveBeenCalledWith('allergene', ['Ei']);
    expect(apiService.setPreferences).toHaveBeenCalledWith('alarm-2024-01-01', ['2024-01-01T08:00:00']);
  });
});
