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


  getAudioData(audioFile: Blob) {
    const formData = new FormData();
    formData.append('file', audioFile, 'recording.ogg');
    return this.http.post(this.apiUrlAudio, formData );
  }

  getPreference(id: string) {
    return this.http.get<{ id: string; value: string[] }>(`${this.apiUrlPrefs}?id=${id}`);
  }

  setPreferences(id: string, value: string[]){
    const body = { id: id, value: value};
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
