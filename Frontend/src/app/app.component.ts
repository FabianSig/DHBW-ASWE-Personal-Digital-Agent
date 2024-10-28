import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SearchBarComponent} from './search-bar/search-bar.component';
import {MenuComponent} from './menu/menu.component';
import {MenuResponse} from './interfaces/menu-response';
import {AudioRecorderComponent} from './audio-recorder/audio-recorder.component';
import {AlarmClockComponent} from './alarm-clock/alarm-clock.component';
import {PreferencesComponent} from './preferences/preferences.component';
import {HeaderComponent} from './header/header.component';
import {ChatComponent} from './chat/chat.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SearchBarComponent, MenuComponent, AudioRecorderComponent, AlarmClockComponent, PreferencesComponent, HeaderComponent, ChatComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  menuResponse?: MenuResponse;

  onMenuResponse(response: MenuResponse): void {
    this.menuResponse = response;
  }


}
