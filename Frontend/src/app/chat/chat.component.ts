import {Component} from '@angular/core';
import {SearchBarComponent} from "../search-bar/search-bar.component";
import {AudioRecorderComponent} from '../audio-recorder/audio-recorder.component';
import {MessageBoxComponent} from '../message-box/message-box.component';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    SearchBarComponent,
    AudioRecorderComponent,
    MessageBoxComponent
  ],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss'
})
export class ChatComponent {
}
