import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TtsService {
  private synth = window.speechSynthesis;

  constructor() {}

  // Method to speak text
  speak(text: string): void {
    if (!this.synth) {
      console.warn('Text-to-Speech not supported in this browser.');
      return;
    }

    // Cancel any ongoing speech
    this.synth.cancel();

    // Create a new speech utterance with the German language
    const utterance = new (window as any).SpeechSynthesisUtterance(text);
    utterance.lang = 'de-DE';  // Set language to German

    this.synth.speak(utterance);
  }
}
