import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SearchBarComponent} from './search-bar/search-bar.component';
import {MenuComponent} from './menu/menu.component';
import {AudioRecorderComponent} from './audio-recorder/audio-recorder.component';
import {AlarmClockComponent} from './alarm-clock/alarm-clock.component';
import {PreferencesComponent} from './preferences/preferences.component';
import {HeaderComponent} from './header/header.component';
import {ChatComponent} from './chat/chat.component';
import {FormsModule} from '@angular/forms';
import {AuthPopupComponent} from './auth-popup/auth-popup.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SearchBarComponent, MenuComponent, AudioRecorderComponent, AlarmClockComponent, PreferencesComponent, HeaderComponent, FormsModule, AuthPopupComponent, ChatComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  chatFullscreen = true;
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

  changeChatFullscreen() {
    this.chatFullscreen = !this.chatFullscreen;
  }

  changePreferencesOpen() {
    this.preferencesOpen = !this.preferencesOpen;
  }
}
