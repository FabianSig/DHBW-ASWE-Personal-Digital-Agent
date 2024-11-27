import { TestBed, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { PreferencesComponent } from './preferences.component';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { TriggerService } from '../services/trigger.service';
import { of, throwError } from 'rxjs';

describe('PreferencesComponent', () => {
  let component: PreferencesComponent;
  let fixture: ComponentFixture<PreferencesComponent>;
  let apiService: jasmine.SpyObj<ApiService>;
  let triggerService: jasmine.SpyObj<TriggerService>;

  beforeEach(() => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['getPreference', 'setPreferences']);
    const triggerServiceSpy = jasmine.createSpyObj('TriggerService', ['reload']);

    TestBed.configureTestingModule({
      declarations: [PreferencesComponent],
      imports: [ReactiveFormsModule],
      providers: [
        FormBuilder,
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: TriggerService, useValue: triggerServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PreferencesComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    triggerService = TestBed.inject(TriggerService) as jasmine.SpyObj<TriggerService>;

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with default values', () => {
    const formValue = component.preferencesForm.value;
    expect(formValue.transportation.mode).toBe('transit');
    expect(formValue.homeAddress.address).toBe('');
    expect(formValue.allergens.Ei).toBeFalse();
  });

  describe('loadPreferences', () => {
    it('should load preferences and patch form values', fakeAsync(() => {
      const mockResponse = { value: ['value1'] };
      apiService.getPreference.and.returnValue(of(mockResponse));

      component['loadPreferences']();
      tick();

      expect(apiService.getPreference).toHaveBeenCalledWith('travelMode');
      expect(component.preferencesForm.get('transportation.mode')?.value).toBe('value1');
    }));

    it('should handle empty responses from API', fakeAsync(() => {
      apiService.getPreference.and.returnValue(of({}));

      component['loadPreferences']();
      tick();

      expect(apiService.getPreference).toHaveBeenCalled();
    }));
  });

  describe('onSubmit', () => {
    it('should call setPreferences and reload trigger service on success', fakeAsync(() => {
      apiService.setPreferences.and.returnValue(of(null));
      spyOn(console, 'log');

      component.onSubmit();
      tick();

      expect(apiService.setPreferences).toHaveBeenCalled();
      expect(triggerService.reload).toHaveBeenCalled();
      expect(console.log).toHaveBeenCalledWith('All preferences updated.');
    }));

    it('should handle errors during preference update', fakeAsync(() => {
      apiService.setPreferences.and.returnValue(throwError(() => new Error('API error')));
      spyOn(console, 'error');

      component.onSubmit();
      tick();

      expect(console.error).toHaveBeenCalledWith('An error occurred while updating preferences:', jasmine.any(Error));
    }));
  });

  describe('getNestedValue', () => {
    it('should correctly retrieve nested values from an object', () => {
      const obj = { level1: { level2: { level3: 'value' } } };
      const result = component['getNestedValue'](obj, 'level1.level2.level3');
      expect(result).toBe('value');
    });

    it('should return undefined for non-existent paths', () => {
      const obj = { level1: { level2: { level3: 'value' } } };
      const result = component['getNestedValue'](obj, 'level1.nonExistent.level3');
      expect(result).toBeUndefined();
    });
  });

  it('should log a warning if form control is not found in loadPreferences', fakeAsync(() => {
    spyOn(console, 'warn');
    apiService.getPreference.and.returnValue(of({ value: ['value1'] }));

    component['loadPreferences']();
    tick();

    expect(console.warn).toHaveBeenCalledWith('Control not found for path: transportation.mode');
  }));
});
