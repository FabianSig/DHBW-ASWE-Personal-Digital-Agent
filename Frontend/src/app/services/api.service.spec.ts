import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ApiService } from './api.service';

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApiService]
    });

    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get ChatGPT data', () => {
    const mockResponse = 'Response from ChatGPT';
    const query = 'Hello ChatGPT';

    service.getChatGPTData(query).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(service['apiUrlChatgpt']);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ message: query });

    req.flush(mockResponse);
  });

  it('should get menu data with specific date', () => {
    const menuDate = '2023-01-01';
    const mockResponse = { menu: 'Pizza' };

    service.getMenuData(menuDate).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${service['apiUrlSpeisekarte']}?datum=${menuDate}`);
    expect(req.request.method).toBe('GET');

    req.flush(mockResponse);
  });

  it('should get menu data without date', () => {
    const mockResponse = { menu: 'Pasta' };

    service.getMenuData("").subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(service['apiUrlSpeisekarte']);
    expect(req.request.method).toBe('GET');

    req.flush(mockResponse);
  });

  it('should post audio data', () => {
    const audioBlob = new Blob(['audio content'], { type: 'audio/ogg' });
    const mockResponse = { result: 'Processed audio' };

    service.getAudioData(audioBlob).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(service['apiUrlAudio']);
    expect(req.request.method).toBe('POST');
    expect(req.request.body instanceof FormData).toBeTrue();

    req.flush(mockResponse);
  });

  it('should set alarm preference', () => {
    const alarmDate = '2023-12-31';
    const alarmTime = '08:00';
    const mockResponse = { status: 'success' };

    service.setAlarmPreference(alarmDate, alarmTime).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(service['apiUrlPrefs']);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ id: alarmDate, value: [alarmTime] });

    req.flush(mockResponse);
  });

  it('should set allergene preference', () => {
    const allergens = ['Ei', 'Fi'];
    const mockResponse = { status: 'success' };

    service.setAllergenePreference(allergens).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(service['apiUrlPrefs']);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ id: 'allergene', value: allergens });

    req.flush(mockResponse);
  });

  it('should get trigger data', () => {
    const date = '2023-01-01';
    const mockResponse = { trigger: 'trigger data' };

    service.getTriggerData(date).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${service['apiUrlTrigger']}?date=${date}`);
    expect(req.request.method).toBe('GET');

    req.flush(mockResponse);
  });

  it('should execute custom trigger routine', () => {
    const route = '/some/custom/route';
    const mockResponse = 'Executed custom routine';

    service.executeCustomTriggerRoutine(route).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${service['baseUrl']}${route}`);
    expect(req.request.method).toBe('GET');

    req.flush(mockResponse);
  });
});
