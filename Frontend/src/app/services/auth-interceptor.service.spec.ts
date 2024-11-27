import { TestBed } from '@angular/core/testing';
import { AuthInterceptorService } from './auth-interceptor.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';

describe('AuthInterceptorService', () => {
  let service: AuthInterceptorService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
      ]
    });

    service = TestBed.inject(AuthInterceptorService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add Authorization header if authKey is set', () => {
    const testKey = 'testAuthKey';
    service.setAuthKey(testKey);

    httpClient.get('/dummy-url').subscribe();

    const httpRequest = httpMock.expectOne('/dummy-url');

    expect(httpRequest.request.headers.has('Authorization')).toBeTrue();
    expect(httpRequest.request.headers.get('Authorization')).toBe(testKey);
  });

  it('should use authKey from localStorage if not explicitly set', () => {
    const testKey = 'storedAuthKey';
    localStorage.setItem('authKey', testKey);

    httpClient.get('/dummy-url').subscribe();

    const httpRequest = httpMock.expectOne('/dummy-url');

    expect(httpRequest.request.headers.has('Authorization')).toBeTrue();
    expect(httpRequest.request.headers.get('Authorization')).toBe(testKey);
  });

  it('should not add Authorization header if authKey is not set', () => {
    httpClient.get('/dummy-url').subscribe();

    const httpRequest = httpMock.expectOne('/dummy-url');

    expect(httpRequest.request.headers.has('Authorization')).toBeFalse();
  });
});
