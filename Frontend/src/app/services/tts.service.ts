import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TtsService {
  speak(text: string, lang: string = 'de-DE') {
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = lang;

    const voices = window.speechSynthesis.getVoices();
    utterance.voice = voices.find(voice => voice.lang === lang) || voices[0];

    window.speechSynthesis.speak(utterance);
  }
}
