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
    const apiSpy = jasmine.createSpyObj('ApiService', ['getTriggerData', 'executeCustomTriggerRoutine']);
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
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should process triggers and set timeouts for future triggers', () => {
    const triggerData = {
      triggers: [
        { route: '/route1', time: new Date(Date.now() + 1000).toISOString() },
        { route: '/route2', time: new Date(Date.now() + 2000).toISOString() },
      ],
    };

    apiServiceSpy.getTriggerData.and.returnValue(of(triggerData));

    service.setOffTrigger();

    expect(apiServiceSpy.getTriggerData).toHaveBeenCalledWith(service.currentDate);
    expect(service['triggerMap']['/route1']).toBeDefined();
    expect(service['triggerMap']['/route2']).toBeDefined();

    // Simulate the passage of time and verify that timeouts will trigger the execution
    jasmine.clock().install();
    jasmine.clock().tick(1001); // Move time forward by 1001 ms
    jasmine.clock().uninstall();

    expect(apiServiceSpy.executeCustomTriggerRoutine).toHaveBeenCalledWith('/route1');
    expect(chatServiceSpy.addMessage).toHaveBeenCalledWith(jasmine.any(String), 'chatgpt');
  });

  it('should clear all triggers', () => {
    const timeoutSpy = spyOn(window, 'clearTimeout');
    service['timoutReferenceMap'] = {
      '/route1': setTimeout(() => {}, 1000),
      '/route2': setTimeout(() => {}, 2000),
    };

    service['clearAllTriggers']();

    expect(timeoutSpy).toHaveBeenCalledTimes(2);
    expect(service['timoutReferenceMap']).toEqual({});
  });

  it('should reload triggers', () => {
    // @ts-ignore
    spyOn(service, 'clearAllTriggers');
    spyOn(service, 'setOffTrigger');

    service.reload();

    expect(service['clearAllTriggers']).toHaveBeenCalled();
    expect(service['setOffTrigger']).toHaveBeenCalled();
  });
});
