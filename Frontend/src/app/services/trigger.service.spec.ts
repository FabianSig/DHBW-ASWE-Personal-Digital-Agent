import { TestBed } from '@angular/core/testing';
import { TriggerService } from './trigger.service';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';
import { of } from 'rxjs';

describe('TriggerService', () => {
  let service: TriggerService;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let chatServiceSpy: jasmine.SpyObj<ChatService>;

  beforeEach(() => {
    const apiSpy = jasmine.createSpyObj('ApiService', ['getTriggerData', 'executeCustomTriggerRoutine', 'getTtsAudioFile']);
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

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('should reload triggers when initialized', () => {
    spyOn(service, 'setOffTrigger').and.callThrough();
    service.reload();
    // @ts-ignore
    expect(service.clearAllTriggers).toHaveBeenCalled();
    expect(service.setOffTrigger).toHaveBeenCalled();
  });

  it('should call setOffTrigger and process triggers', () => {
    const mockData = { triggers: [{ route: 'testRoute', time: new Date(Date.now() + 10000).toISOString() }] };
    apiServiceSpy.getTriggerData.and.returnValue(of(mockData));

    service.setOffTrigger();
    expect(apiServiceSpy.getTriggerData).toHaveBeenCalledWith(service.currentDate);
    // @ts-ignore
    expect(service.triggerMap['testRoute']).toBeGreaterThan(0); // Ensure the trigger time is set
  });

  // Additional tests can be added here for other methods
});
