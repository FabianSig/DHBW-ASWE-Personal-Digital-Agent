import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { TriggerService } from './trigger.service';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';

describe('TriggerService', () => {
  let service: TriggerService;
  let apiService: jasmine.SpyObj<ApiService>;
  let chatService: jasmine.SpyObj<ChatService>;

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
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    chatService = TestBed.inject(ChatService) as jasmine.SpyObj<ChatService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call setOffTrigger on reload', () => {
    spyOn(service, 'setOffTrigger').and.callThrough();
    service.reload();
    expect(service.setOffTrigger).toHaveBeenCalled();
  });

  it('should fetch and process triggers', () => {
    const mockData = { triggers: [{ route: 'testRoute', time: new Date(Date.now() + 10000).toISOString() }] };
    apiService.getTriggerData.and.returnValue(of(mockData));

    service.setOffTrigger();

    expect(apiService.getTriggerData).toHaveBeenCalledWith(service.currentDate);
    // @ts-ignore
    expect(service.triggerMap['testRoute']).toBeDefined();
    // @ts-ignore
    expect(service.triggerMap['testRoute']).toBeGreaterThan(0);
  });

  it('should execute triggers when time is valid', () => {
    const mockData = { triggers: [{ route: 'testRoute', time: new Date(Date.now() + 10000).toISOString() }] };
    apiService.getTriggerData.and.returnValue(of(mockData));
    apiService.executeCustomTriggerRoutine.and.returnValue(of('audio text'));

    service.setOffTrigger();
    // @ts-ignore
    service.processTriggers(); // Simulate trigger processing

    expect(apiService.executeCustomTriggerRoutine).toHaveBeenCalledWith('testRoute');
  });

  it('should handle errors when fetching audio', () => {
    const mockData = { triggers: [{ route: 'testRoute', time: new Date(Date.now() + 10000).toISOString() }] };
    apiService.getTriggerData.and.returnValue(of(mockData));
    apiService.executeCustomTriggerRoutine.and.returnValue(of('audio text'));
    apiService.getTtsAudioFile.and.returnValue(throwError('Error fetching audio'));

    service.setOffTrigger();
    // @ts-ignore
    service.processTriggers(); // Simulate trigger processing

    expect(console.error).toHaveBeenCalledWith('Error occurred:', 'Error fetching audio');
  });

  afterEach(() => {
    // Reset the spies after each test
    apiService.getTriggerData.calls.reset();
    apiService.executeCustomTriggerRoutine.calls.reset();
    apiService.getTtsAudioFile.calls.reset();
    chatService.addMessage.calls.reset();
  });
});
