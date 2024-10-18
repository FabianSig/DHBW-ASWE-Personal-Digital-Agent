import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrlChatgpt = environment.apiUrlChatgpt;
  private apiUrlSpeisekarte = environment.apiUrlSpeisekarte;

  constructor(private http: HttpClient) {}

  getChatGPTData(query: string, key: string) {
    const headers = { 'Authorization': key }
    const body = { message: query };
    return this.http.post(this.apiUrlChatgpt, body, {headers} );
  }

  getMenuData(menuDate: string) {
    // If no date is given, undefined is used to display the menu for the current day
    const params = menuDate ? { datum : menuDate } : undefined;
    return this.http.get(this.apiUrlSpeisekarte, { params });
  }
}
