import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {PreferencesComponent} from './preferences/preferences.component';
import {HeaderComponent} from './header/header.component';
import {FormsModule} from '@angular/forms';
import {AuthPopupComponent} from './auth-popup/auth-popup.component';
import {ChatBoxComponent} from './chat-box/chat-box.component';
import {ChatInputComponent} from './chat-input/chat-input.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, PreferencesComponent, HeaderComponent, FormsModule, AuthPopupComponent, ChatBoxComponent, ChatInputComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  preferencesOpen = false; // Tracks whether the preferences panel is open

  ngOnInit() {
    // Checks for the authentication key on component initialization
    this.checkForAuthKey();
  }

  // Verifies if an authentication key is stored in local storage and shows the popup to five the key if it is not
  checkForAuthKey() {
    const authKey = localStorage.getItem('authKey');
    if (!authKey) {
      this.blurApp();
      this.showPopup();
    }
  }

  // Adds a blur effect to the app container for visual feedback
  blurApp() {
    const appElement = document.querySelector('.app-container');
    if (appElement) {
      appElement.classList.add('blurred'); // Applies a blurred style to the app
    }
  }

  // Reveals the popup overlay for user authentication
  showPopup() {
    const popupElement = document.querySelector('.popup-overlay');
    if (popupElement) {
      popupElement.classList.remove('hidden'); // Makes the popup visible
    }
  }

  // Toggles the visibility of the preferences panel
  changePreferencesOpen() {
    this.preferencesOpen = !this.preferencesOpen;
  }
}
