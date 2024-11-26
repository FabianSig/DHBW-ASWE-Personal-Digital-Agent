import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        provideHttpClient(withInterceptorsFromDi())
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should check for auth key and take action if not present', () => {
    spyOn(localStorage, 'getItem').and.returnValue(null);
    spyOn(component, 'blurApp');
    spyOn(component, 'showPopup');

    component.checkForAuthKey();

    expect(component.blurApp).toHaveBeenCalled();
    expect(component.showPopup).toHaveBeenCalled();
  });

  it('should blur app properly', () => {
    const appElement = document.createElement('div');
    appElement.classList.add('app-container');
    document.body.appendChild(appElement);

    component.blurApp();
    expect(appElement.classList.contains('blurred')).toBeTrue();

    document.body.removeChild(appElement);
  });

  it('should show popup properly', () => {
    const popupElement = document.createElement('div');
    popupElement.classList.add('popup-overlay', 'hidden');
    document.body.appendChild(popupElement);

    component.showPopup();

    // Use fixture.detectChanges() to ensure that changes are reflected
    fixture.detectChanges();
    expect(popupElement.classList.contains('hidden')).toBeFalse();

    document.body.removeChild(popupElement);
  });

  it('should toggle preferencesOpen', () => {
    const initialPreferenceState = component.preferencesOpen;
    component.changePreferencesOpen();
    expect(component.preferencesOpen).toBe(!initialPreferenceState);
  });
});
