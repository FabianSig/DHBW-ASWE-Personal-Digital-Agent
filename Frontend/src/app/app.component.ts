import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SearchBarComponent} from './search-bar/search-bar.component';
import {ChatGPTResponse} from './interfaces/chat-gptresponse';
import {MenuComponent} from './menu/menu.component';
import {MenuResponse} from './interfaces/menu-response';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SearchBarComponent, MenuComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Personal-Digital-Agent-Frontend';
  chatGPTResponse?: ChatGPTResponse;
  menuResponse?: MenuResponse;

  onChatGPTResponse(response: ChatGPTResponse): void {
    this.chatGPTResponse = response;
  }

  onMenuResponse(response: MenuResponse): void {
    this.menuResponse = response;
  }
}
