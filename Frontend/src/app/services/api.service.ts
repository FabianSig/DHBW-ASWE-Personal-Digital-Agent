import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrlChatgpt = environment.apiUrlChatgpt;
  private apiUrlSpeisekarte = environment.apiUrlSpeisekarte;
  private apiUrlAudio = environment.apiUrlAudio;

  constructor(private http: HttpClient) {}

  getChatGPTData(query: string, key: string) {
    const headers = { 'Authorization': key };
    const body = { message: query };

    console.log(`Sending POST request to ChatGPT API at: ${this.apiUrlChatgpt}`, { body, headers });

    return this.http.post(this.apiUrlChatgpt, body);
  }

  getMenuData(menuDate: string) {
    const params = menuDate ? { datum: menuDate } : undefined;

    console.log(`Sending GET request to Menu API at: ${this.apiUrlSpeisekarte}`, { params });

    return this.http.get(this.apiUrlSpeisekarte, { params });
  }

  getAudioData(audioFile: Blob, key: string) {
    const headers = { 'Authorization': key };
    const formData = new FormData();
    formData.append('file', audioFile, 'recording.ogg');

    console.log(`Sending POST request to Audio API at: ${this.apiUrlAudio}`, { headers, formData });

    return this.http.post(this.apiUrlAudio, formData, { headers });
  }
}
