import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrlChatgpt = "http://localhost:8080" + environment.apiUrlLogic;
  private apiUrlSpeisekarte = "http://localhost:8080" + environment.apiUrlSpeisekarte;
  private apiUrlAudio = "http://localhost:8080" + environment.apiUrlAudio;
  private apiUrlPrefs = "http://localhost:8080" + environment.apiUrlPrefs;

  constructor(private http: HttpClient) {}

  getChatGPTData(query: string) {
    const body = { message: query };
    return this.http.post<string>(this.apiUrlChatgpt, body , { responseType: 'text' as 'json' });
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

  setAlarmPreference(alarmDate: string, alarmTime: string) {
    const body = { id: alarmDate, value: [alarmTime]};
    return this.http.post(this.apiUrlPrefs, body);
  }

  setAllergenePreference(allergene: string[]) {
    const body = { id: "allergene", value: allergene};
    return this.http.post(this.apiUrlPrefs, body);
  }
}
