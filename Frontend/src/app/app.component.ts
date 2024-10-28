import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SearchBarComponent} from './search-bar/search-bar.component';
import {MenuComponent} from './menu/menu.component';
import {MenuResponse} from './interfaces/menu-response';
import {AudioRecorderComponent} from './audio-recorder/audio-recorder.component';
import {ChatGPTResponse} from './interfaces/chat-gptresponse';
import {AudioResponse} from './interfaces/audio-response';
import {AlarmClockComponent} from './alarm-clock/alarm-clock.component';
import {PreferencesComponent} from './preferences/preferences.component';
import {HeaderComponent} from './header/header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SearchBarComponent, MenuComponent, AudioRecorderComponent, AlarmClockComponent, PreferencesComponent, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  chatGPTResponse?: ChatGPTResponse;
  menuResponse?: MenuResponse;
  audioResponse?: AudioResponse;

  onChatGPTResponse(response: ChatGPTResponse): void {
    this.chatGPTResponse = response;
  }

  onMenuResponse(response: MenuResponse): void {
    this.menuResponse = response;
  }

  onAudioResponse(response: AudioResponse): void {
    this.audioResponse = response;
  }
}
