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
  preferencesOpen = false;

  ngOnInit() {
    this.checkForAuthKey();
  }

  checkForAuthKey() {
    const authKey = localStorage.getItem('authKey');
    if (!authKey) {
      this.blurApp();
      this.showPopup();
    }
  }

  blurApp() {
    const appElement = document.querySelector('.app-container');
    if (appElement) {
      appElement.classList.add('blurred');
    }
  }

  showPopup() {
    const popupElement = document.querySelector('.popup-overlay');
    if (popupElement) {
      popupElement.classList.remove('hidden');
    }
  }

  changePreferencesOpen() {
    this.preferencesOpen = !this.preferencesOpen;
  }
}
