import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { TriggerService } from './trigger.service';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';

describe('TriggerService', () => {
  let service: TriggerService;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let chatServiceSpy: jasmine.SpyObj<ChatService>;

  beforeEach(() => {
    const apiSpy = jasmine.createSpyObj('ApiService', {'getTriggerData': of({ triggers: [] }), 'executeCustomTriggerRoutine': of('Trigger executed')});
    const chatSpy = jasmine.createSpyObj('ChatService', ['addMessage']);

    TestBed.configureTestingModule({
      providers: [
        TriggerService,
        { provide: ApiService, useValue: apiSpy },
        { provide: ChatService, useValue: chatSpy },
      ],
    });

    service = TestBed.inject(TriggerService);
    apiServiceSpy = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    chatServiceSpy = TestBed.inject(ChatService) as jasmine.SpyObj<ChatService>;

    // Mocking API responses
    apiServiceSpy.getTriggerData.and.returnValue(of({ triggers: [] }));
    apiServiceSpy.executeCustomTriggerRoutine.and.returnValue(of('Trigger executed'));
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
