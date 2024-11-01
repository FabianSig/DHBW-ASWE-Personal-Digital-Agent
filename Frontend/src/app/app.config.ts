import { ApplicationConfig, provideZoneChangeDetection, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import { provideServiceWorker } from '@angular/service-worker';
import {AuthInterceptorService} from './services/auth-interceptor.service';
import {MessageBoxComponent} from './message-box/message-box.component';

export const appConfig: ApplicationConfig = {
  providers: [ {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}, provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), provideHttpClient(withInterceptorsFromDi()), provideServiceWorker('ngsw-worker.js', {
            enabled: !isDevMode(),
            registrationStrategy: 'registerWhenStable:30000'
          }), MessageBoxComponent]
};
