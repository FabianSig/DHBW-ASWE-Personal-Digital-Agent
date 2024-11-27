import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { TriggerService } from './trigger.service';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';

describe('TriggerService', () => {
  let service: TriggerService;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let chatServiceSpy: jasmine.SpyObj<ChatService>;

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

    service = TestBed.inject(TriggerService);
    apiServiceSpy = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    chatServiceSpy = TestBed.inject(ChatService) as jasmine.SpyObj<ChatService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should clear all triggers on reload', () => {
    spyOn(service as any, 'clearAllTriggers').and.callThrough();
    spyOn(service as any, 'setOffTrigger').and.callThrough();

    service.reload();

    expect(service['clearAllTriggers']).toHaveBeenCalled();
    expect(service['setOffTrigger']).toHaveBeenCalled();
  });

  it('should set up triggers and call processTriggers on setOffTrigger', () => {
    const mockData = { triggers: [{ route: '/routine1', time: new Date().getTime() + 10000 }] };
    apiServiceSpy.getTriggerData.and.returnValue(of(mockData));
    spyOn(service as any, 'processTriggers').and.callThrough();

    service['setOffTrigger']();

    expect(apiServiceSpy.getTriggerData).toHaveBeenCalledWith(service.currentDate);
    expect(service['processTriggers']).toHaveBeenCalled();
  });

  it('should execute trigger routine and play audio on processTriggers', () => {
    const mockTriggerMap = { '/routine1': new Date().getTime() + 5000 };
    const mockAudioBlob = new Blob(['test audio']);
    service['triggerMap'] = mockTriggerMap;

    apiServiceSpy.executeCustomTriggerRoutine.and.returnValue(of('Trigger executed'));
    apiServiceSpy.getTtsAudioFile.and.returnValue(of(mockAudioBlob));

    service['processTriggers']();

    expect(apiServiceSpy.executeCustomTriggerRoutine).toHaveBeenCalledWith('/routine1');
    expect(apiServiceSpy.getTtsAudioFile).toHaveBeenCalledWith('Trigger executed');
  });

  it('should handle audio playback correctly in handleTriggerDisplayText', () => {
    const mockBlob = new Blob(['audio']);
    const text = 'Test message';
    const timeDifference = 1000;

    apiServiceSpy.getTtsAudioFile.and.returnValue(of(mockBlob));

    spyOn(window, 'setTimeout').and.callThrough();

    service['handleTriggerDisplayText'](text, timeDifference);

    expect(apiServiceSpy.getTtsAudioFile).toHaveBeenCalledWith(text);
    expect(window.setTimeout).toHaveBeenCalled();
  });

  it('should handle API errors gracefully', () => {
    const mockError = new Error('API Error');

    apiServiceSpy.getTriggerData.and.returnValue(throwError(() => mockError));

    service['setOffTrigger']();

    expect(apiServiceSpy.getTriggerData).toHaveBeenCalled();
  });

  it('should clear all timeout references in clearAllTriggers', () => {
    const mockTimeoutMap = {
      '/routine1': setTimeout(() => {}, 5000),
      '/routine2': setTimeout(() => {}, 10000)
    };

    service['timoutReferenceMap'] = mockTimeoutMap;

    spyOn(window, 'clearTimeout').and.callThrough();

    service['clearAllTriggers']();

    expect(window.clearTimeout).toHaveBeenCalledTimes(2);
    expect(service['timoutReferenceMap']).toEqual({});
  });
});
