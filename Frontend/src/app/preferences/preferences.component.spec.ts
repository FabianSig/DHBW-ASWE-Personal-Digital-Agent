import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PreferencesComponent } from './preferences.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { TriggerService } from '../services/trigger.service';

describe('PreferencesComponent', () => {
  let component: PreferencesComponent;
  let fixture: ComponentFixture<PreferencesComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let triggerServiceSpy: jasmine.SpyObj<TriggerService>;

  beforeEach(async () => {
    apiServiceSpy = jasmine.createSpyObj('ApiService', ['setAlarmPreference']);
    triggerServiceSpy = jasmine.createSpyObj('TriggerService', ['reload']);

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
});
