import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getChatGPTData(query: string, key: string) {
    const headers = { 'Authorization': key }
    const body = { message: query };
    return this.http.post(this.apiUrl, body, {headers} );
  }
}
