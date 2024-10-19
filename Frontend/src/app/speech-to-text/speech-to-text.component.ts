import { Component, NgZone } from '@angular/core';

declare var webkitSpeechRecognition: new () => any;

@Component({
  selector: 'app-speech-to-text',
  standalone: true,
  imports: [],
  templateUrl: './speech-to-text.component.html',
  styleUrl: './speech-to-text.component.scss'
})
export class SpeechToTextComponent {
  speechToTextResult?: string;

  constructor(private ngZone: NgZone) {}

  startListening() {
    if ('webkitSpeechRecognition' in window) {
      const vSearch = new webkitSpeechRecognition();
      vSearch.continuous = false;
      vSearch.interimresults = false;
      vSearch.lang = 'de-DE';
      vSearch.start();
      vSearch.onresult = (e: any) => {
        this.ngZone.run(() => {
          this.speechToTextResult = e.results[0][0].transcript;
        });
        vSearch.stop();
      };
    } else {
      alert('Your browser does not support voice recognition!');
    }
  }
}
