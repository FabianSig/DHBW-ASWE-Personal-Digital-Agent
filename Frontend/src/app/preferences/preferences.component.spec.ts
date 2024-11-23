import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PreferencesComponent } from './preferences.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { TriggerService } from '../services/trigger.service';
import { of } from 'rxjs';

// Mocks for localStorage
function mockLocalStorage(): Storage {
  let store: { [key: string]: string } = {};

  return {
    getItem: (key: string): string | null => store[key] || null,
    setItem: (key: string, value: string) => {
      store[key] = value;
    },
    removeItem: (key: string): void => {
      delete store[key];
    },
    clear: (): void => {
      store = {};
    },
    key: (index: number): string | null => {
      const keys = Object.keys(store);
      return keys[index] || null;
    },
    get length() {
      return Object.keys(store).length;
    },
  } as Storage;
}

describe('PreferencesComponent', () => {
  let component: PreferencesComponent;
  let fixture: ComponentFixture<PreferencesComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let triggerServiceSpy: jasmine.SpyObj<TriggerService>;
  let mockStorage: Storage;

  beforeEach(async () => {
    apiServiceSpy = jasmine.createSpyObj('ApiService', ['setAlarmPreference', 'setAllergenePreference']);
    triggerServiceSpy = jasmine.createSpyObj('TriggerService', ['reload']);

    mockStorage = mockLocalStorage() as Storage;
    spyOn(localStorage, 'getItem').and.callFake(mockStorage.getItem);
    spyOn(localStorage, 'setItem').and.callFake(mockStorage.setItem);
    spyOn(localStorage, 'removeItem').and.callFake(mockStorage.removeItem);
    spyOn(localStorage, 'clear').and.callFake(mockStorage.clear);

    await TestBed.configureTestingModule({
      imports: [PreferencesComponent, ReactiveFormsModule],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: TriggerService, useValue: triggerServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PreferencesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load preferences on init from localStorage', () => {
    const preferencesMock = JSON.stringify({
      transportation: { onFoot: true, byBike: false, byCar: true, byPublicTransport: false },
      address: { street: 'Test St', city: 'Test City', zip: '12345', country: 'Testland' },
      reminder: 'Test reminder',
      alarm: { alarmDate: '2023-12-31', alarmTime: '12:00' },
      allergens: { Ei: true, En: false }
    });

    localStorage.setItem('preferences', preferencesMock);
    component.ngOnInit();

    expect(component.preferencesForm.value).toEqual(JSON.parse(preferencesMock));
  });

  it('should save preferences and set alarm and allergens on submit', () => {
    component.preferencesForm.setValue({
      transportation: { onFoot: true, byBike: false, byCar: true, byPublicTransport: false },
      address: { street: 'Test St', city: 'Test City', zip: '12345', country: 'Testland' },
      reminder: 'Test reminder',
      alarm: { alarmDate: '2023-12-31', alarmTime: '12:00' },
      allergens: { Ei: true, En: false, Fi: true }
    });

    apiServiceSpy.setAlarmPreference.and.returnValue(of({}));
    apiServiceSpy.setAllergenePreference.and.returnValue(of({}));

    component.onSubmit();

    const formData = component.preferencesForm.value;
    expect(localStorage.getItem('preferences')).toBe(JSON.stringify(formData));

    const alarmId = "wecker-2023-12-31";
    const alarmValue = "2023-12-31T12:00:00";
    expect(apiServiceSpy.setAlarmPreference).toHaveBeenCalledWith(alarmId, alarmValue);
    expect(apiServiceSpy.setAllergenePreference).toHaveBeenCalledWith(['Ei', 'Fi']);
    expect(triggerServiceSpy.reload).toHaveBeenCalled();
  });
});
