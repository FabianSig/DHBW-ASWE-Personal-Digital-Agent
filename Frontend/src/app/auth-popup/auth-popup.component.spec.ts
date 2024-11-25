import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthPopupComponent } from './auth-popup.component';
import { FormsModule } from '@angular/forms';
import { AuthInterceptorService } from '../services/auth-interceptor.service';

describe('AuthPopupComponent', () => {
  let component: AuthPopupComponent;
  let fixture: ComponentFixture<AuthPopupComponent>;
  let authInterceptorServiceSpy: jasmine.SpyObj<AuthInterceptorService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('AuthInterceptorService', ['setAuthKey']);

    await TestBed.configureTestingModule({
      imports: [AuthPopupComponent, FormsModule],
      providers: [
        { provide: AuthInterceptorService, useValue: spy }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AuthPopupComponent);
    component = fixture.componentInstance;
    authInterceptorServiceSpy = TestBed.inject(AuthInterceptorService) as jasmine.SpyObj<AuthInterceptorService>;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set auth key and close popup if authKey is provided', () => {
    component.authKey = 'testKey';

    spyOn(component, 'closePopup');

    component.submitKey();

    expect(authInterceptorServiceSpy.setAuthKey).toHaveBeenCalledWith('testKey');
    expect(component.closePopup).toHaveBeenCalled();
  });

  it('should not set auth key or close popup if authKey is empty', () => {
    component.authKey = '';

    spyOn(component, 'closePopup');

    component.submitKey();

    expect(authInterceptorServiceSpy.setAuthKey).not.toHaveBeenCalled();
    expect(component.closePopup).not.toHaveBeenCalled();
  });

  it('should hide the popup and remove blur effect when closePopup is called', () => {
    const popupElement = document.createElement('div');
    popupElement.classList.add('popup-overlay');
    document.body.appendChild(popupElement);

    const appElement = document.createElement('div');
    appElement.classList.add('app-container', 'blurred');
    document.body.appendChild(appElement);

    component.closePopup();
    fixture.detectChanges();  // Ensure that the changes are reflected

    expect(popupElement.classList.contains('hidden')).toBeTrue();
    expect(appElement.classList.contains('blurred')).toBeFalse();

    // Cleanup
    document.body.removeChild(popupElement);
    document.body.removeChild(appElement);
  });
});
