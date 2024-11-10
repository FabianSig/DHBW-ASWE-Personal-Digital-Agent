import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SearchBarComponent} from './search-bar/search-bar.component';
import {MenuComponent} from './menu/menu.component';
import {MenuResponse} from './interfaces/menu-response';
import {AudioRecorderComponent} from './audio-recorder/audio-recorder.component';
import {AlarmClockComponent} from './alarm-clock/alarm-clock.component';
import {PreferencesComponent} from './preferences/preferences.component';
import {HeaderComponent} from './header/header.component';
import {ChatComponent} from './chat/chat.component';
import {FormsModule} from '@angular/forms';
import {AuthPopupComponent} from './auth-popup/auth-popup.component';
import {TriggerService} from './services/trigger.service';
import {ApiService} from './services/api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SearchBarComponent, MenuComponent, AudioRecorderComponent, AlarmClockComponent, PreferencesComponent, HeaderComponent, FormsModule, AuthPopupComponent, ChatComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  menuResponse?: MenuResponse;
  chatFullscreen = true;
  preferencesOpen = false;

  constructor(private triggerService: TriggerService, private apiService: ApiService) {
  }

  onMenuResponse(response: MenuResponse): void {
    this.menuResponse = response;
  }

  ngOnInit() {
    this.checkForAuthKey();
    this.triggerService.setOffTrigger();
    this.apiService.getTriggerData("2024-11-8").subscribe((data) =>{
      console.log(data);
    })
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
