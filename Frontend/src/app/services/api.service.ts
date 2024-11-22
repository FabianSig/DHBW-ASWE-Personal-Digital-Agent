import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

const localHost = "http://localhost:8080";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = localHost
  private apiUrlChatgpt = localHost + '/api/logic/message'
  private apiUrlSpeisekarte = localHost + '/api/speisekarte';
  private apiUrlAudio = localHost + '/api/chatgpt/audio';
  private apiUrlPrefs = localHost + '/api/prefs';
  private apiUrlTrigger = localHost + '/api/logic/trigger';
  private apiUrlTts = localHost + '/api/chatgpt/tts';

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

  getTriggerData(date: string) {
    return this.http.get(this.apiUrlTrigger, {
      params: {date: date}
    });
  }

  executeCustomTriggerRoutine(route: string) {
    return this.http.get(this.baseUrl + route, { responseType: 'text' });
  }

  getTtsAudioFile(text: string) {
    return this.http.post(this.apiUrlTts, {text}, { responseType: 'blob' });
  }
}
