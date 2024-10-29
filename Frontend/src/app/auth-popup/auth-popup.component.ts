import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthInterceptorService} from '../services/auth-interceptor.service';

@Component({
  selector: 'app-auth-popup',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './auth-popup.component.html',
  styleUrl: './auth-popup.component.scss'
})
export class AuthPopupComponent {
  authKey: string = '';

  constructor(private authInterceptorService: AuthInterceptorService) {}

  submitKey() {
    if (this.authKey) {
      this.authInterceptorService.setAuthKey(this.authKey);
      this.closePopup();
    }
  }

  closePopup() {
    // Hide the popup
    const popupElement = document.querySelector('.popup-overlay');
    if (popupElement) {
      popupElement.classList.add('hidden');
    }
    // Remove blur effect from the application
    const appElement = document.querySelector('.app-container');
    if (appElement) {
      appElement.classList.remove('blurred');
    }
  }
}
