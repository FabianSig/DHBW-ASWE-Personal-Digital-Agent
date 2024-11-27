import { TestBed } from '@angular/core/testing';
import { TriggerService } from './trigger.service';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';

describe('TriggerService', () => {
  let service: TriggerService;

  beforeEach(() => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', [
      'getTriggerData',
      'executeCustomTriggerRoutine',
      'getTtsAudioFile'
    ]);
    const chatServiceSpy = jasmine.createSpyObj('ChatService', ['addMessage']);

    TestBed.configureTestingModule({
      providers: [
        TriggerService,
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: ChatService, useValue: chatServiceSpy }
      ]
    });

    service = TestBed.inject(TriggerService);
  });

  it('should create the service', () => {
    expect(service).toBeTruthy();
  });
});
