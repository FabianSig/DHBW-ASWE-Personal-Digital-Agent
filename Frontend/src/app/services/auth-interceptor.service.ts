import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor  {
  private authKey: string = '';

  // Sets the authentication key and saves it to local storage for persistence
  setAuthKey(key: string) {
    this.authKey = key;
    localStorage.setItem('authKey', key);
  }

  // Intercepts HTTP requests to add the authentication key if available
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Retrieve the authKey from local storage if it hasn't been set yet
    this.authKey = this.authKey || localStorage.getItem('authKey') || '';

    if (this.authKey) {
      // Clone the request to avoid mutating the original request object
      const clonedRequest = req.clone({
        setHeaders: {
          'Authorization': this.authKey
        }
      });
      return next.handle(clonedRequest); // Forward the modified request
    }

    return next.handle(req); // Forward the original request if no auth key is present
  }
}
