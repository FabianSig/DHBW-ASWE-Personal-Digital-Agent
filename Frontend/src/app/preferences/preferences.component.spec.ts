import { TestBed, ComponentFixture } from '@angular/core/testing';
import { PreferencesComponent } from './preferences.component';
import { ApiService } from '../services/api.service';
import { TriggerService } from '../services/trigger.service';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

describe('PreferencesComponent', () => {
  let component: PreferencesComponent;
  let fixture: ComponentFixture<PreferencesComponent>;

  beforeEach(() => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['getPreference', 'setPreferences']);
    const triggerServiceSpy = jasmine.createSpyObj('TriggerService', ['reload']);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [PreferencesComponent],
      providers: [
        FormBuilder,
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: TriggerService, useValue: triggerServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PreferencesComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
