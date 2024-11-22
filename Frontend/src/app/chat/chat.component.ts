import {Component} from '@angular/core';
import {SearchBarComponent} from "../search-bar/search-bar.component";
import {MessageBoxComponent} from '../message-box/message-box.component';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    SearchBarComponent,
    MessageBoxComponent
  ],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss'
})
export class ChatComponent {
}
