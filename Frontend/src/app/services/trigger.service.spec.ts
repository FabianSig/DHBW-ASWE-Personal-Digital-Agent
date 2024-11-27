import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { TriggerService } from './trigger.service';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';

describe('TriggerService', () => {

  beforeEach(() => {
    const apiSpy = jasmine.createSpyObj('ApiService', [
      'getTriggerData',
      'executeCustomTriggerRoutine',
      'getTtsAudioFile'
    ]);
    const chatSpy = jasmine.createSpyObj('ChatService', ['addMessage']);

    TestBed.configureTestingModule({
      providers: [
        TriggerService,
        { provide: ApiService, useValue: apiSpy },
        { provide: ChatService, useValue: chatSpy }
      ]
    });
  });

  it('should be created', () => {
    const service = TestBed.inject(TriggerService);
    expect(service).toBeTruthy();
  });

  it('should get trigger data', () => {
    const service = TestBed.inject(TriggerService);
    const apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    const date = '2023-01-01';
    const mockResponse = { trigger: 'trigger data' };
    apiService.getTriggerData.and.returnValue(of(mockResponse));

    service.setOffTrigger();

    expect(apiService.getTriggerData).toHaveBeenCalledWith(date);
  });

  it('should handle API errors gracefully', () => {
    const service = TestBed.inject(TriggerService);
    const apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    apiService.getTriggerData.and.returnValue(throwError(() => new Error('API Error')));
    spyOn(console, 'warn');

    service.setOffTrigger();

    expect(console.warn).toHaveBeenCalledWith(jasmine.any(String));
  });
});
