import { TestBed, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { PreferencesComponent } from './preferences.component';
import { ApiService } from '../services/api.service';
import { TriggerService } from '../services/trigger.service';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('loadPreferences', () => {
    it('should load preferences and update form controls', fakeAsync(() => {
      const mockResponse = { id: 'travelMode', value: ['transit'] };
      apiService.getPreference.and.returnValue(of(mockResponse));

      component['loadPreferences']();
      tick();

      expect(apiService.getPreference).toHaveBeenCalledWith('travelMode');
      expect(component.preferencesForm.get('transportation.mode')?.value).toBe('transit');
    }));

  });

  describe('onSubmit', () => {
    it('should submit preferences and call API service', fakeAsync(() => {
      const mockResponse = { id: 'travelMode', value: ['transit'] };
      apiService.setPreferences.and.returnValue(of({}));

      component.onSubmit();
      tick();

      expect(apiService.setPreferences).toHaveBeenCalled();
      expect(triggerService.reload).toHaveBeenCalled();
    }));

    it('should handle errors during preference submission', fakeAsync(() => {
      spyOn(console, 'error');
      apiService.setPreferences.and.returnValue(throwError(() => new Error('API Error')));

      component.onSubmit();
      tick();

      expect(console.error).toHaveBeenCalledWith('An error occurred while updating preferences:', jasmine.any(Error));
    }));
  });


});
