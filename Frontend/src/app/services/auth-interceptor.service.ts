import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor  {
  private authKey: string = '';

  setAuthKey(key: string) {
    this.authKey = key;
    localStorage.setItem('authKey', key);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.authKey) {
      this.authKey = localStorage.getItem('authKey')!;

      const clonedRequest = req.clone({
        setHeaders: {
          'Authorization': this.authKey
        }
      });
      return next.handle(clonedRequest);
    }

    return next.handle(req);
  }
}
