import {ApplicationConfig, provideZoneChangeDetection, isDevMode, importProvidersFrom} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import { provideServiceWorker } from '@angular/service-worker';
import {AuthInterceptorService} from './services/auth-interceptor.service';
import {ChatBoxComponent} from './chat-box/chat-box.component';
import {MarkdownModule} from 'ngx-markdown';
import {provideHttpClientTesting} from '@angular/common/http/testing';

export const appConfig: ApplicationConfig = {
  providers: [ {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}, provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), provideHttpClient(withInterceptorsFromDi()), provideServiceWorker('ngsw-worker.js', {
            enabled: !isDevMode(),
            registrationStrategy: 'registerWhenStable:30000'
          }), ChatBoxComponent, importProvidersFrom(MarkdownModule.forRoot()), provideHttpClientTesting() ]
};
