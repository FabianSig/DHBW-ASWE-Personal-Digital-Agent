import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrlChatgpt = environment.apiUrlChatgpt;
  private apiUrlSpeisekarte = environment.apiUrlSpeisekarte;
  private apiUrlAudio = environment.apiUrlAudio;

  constructor(private http: HttpClient) {}

  getChatGPTData(query: string) {
    const body = { message: query };
    return this.http.post(this.apiUrlChatgpt, body );
  }

  getMenuData(menuDate: string) {
    // If no date is given, undefined is used to display the menu for the current day
    const params = menuDate ? { datum : menuDate } : undefined;
    return this.http.get(this.apiUrlSpeisekarte, { params });
  }

  getAudioData(audioFile: Blob) {
    const formData = new FormData();
    formData.append('file', audioFile, 'recording.ogg');
    return this.http.post(this.apiUrlAudio, formData );
  }
}
