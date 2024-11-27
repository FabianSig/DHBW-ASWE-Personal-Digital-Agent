import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { TriggerService } from './trigger.service';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';
import { of, throwError } from 'rxjs';

describe('TriggerService', () => {
  let service: TriggerService;
  let apiService: jasmine.SpyObj<ApiService>;
  let chatService: jasmine.SpyObj<ChatService>;

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
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    chatService = TestBed.inject(ChatService) as jasmine.SpyObj<ChatService>;
  });

  it('should create the service', () => {
    expect(service).toBeTruthy();
  });

  describe('reload', () => {
    it('should clear all triggers and set new ones', fakeAsync(() => {
      spyOn(service as any, 'clearAllTriggers');
      spyOn(service as any, 'setOffTrigger');

      service.reload();

      expect(service['clearAllTriggers']).toHaveBeenCalled();
      expect(service['setOffTrigger']).toHaveBeenCalled();
    }));
  });

  describe('setOffTrigger', () => {
    it('should fetch trigger data and update the triggerMap', fakeAsync(() => {
      const mockTriggerData = {
        triggers: [{ route: 'testRoute', time: new Date().getTime() + 5000 }]
      };
      apiService.getTriggerData.and.returnValue(of(mockTriggerData));

      service.setOffTrigger();
      tick();

      expect(apiService.getTriggerData).toHaveBeenCalledWith(service.currentDate);
      expect(service['triggerMap']['testRoute']).toBeDefined();
    }));

    it('should handle errors while fetching trigger data', fakeAsync(() => {
      spyOn(console, 'error');
      apiService.getTriggerData.and.returnValue(throwError(() => new Error('API error')));

      service.setOffTrigger();
      tick();

      expect(console.error).toHaveBeenCalledWith('Error occurred:', jasmine.any(Error));
    }));
  });

  describe('processTriggers', () => {
    it('should process triggers and execute routines', fakeAsync(() => {
      service['triggerMap'] = { testRoute: new Date().getTime() + 1000 };
      apiService.executeCustomTriggerRoutine.and.returnValue(of('Routine Executed'));

      spyOn(console, 'log');
      service['processTriggers']();
      tick();

      expect(apiService.executeCustomTriggerRoutine).toHaveBeenCalledWith('testRoute');
      expect(console.log).toHaveBeenCalledWith('Processing testRoute with timedifference', jasmine.any(Number));
    }));

    it('should handle triggers with past times gracefully', fakeAsync(() => {
      service['triggerMap'] = { pastRoute: new Date().getTime() - 1000 };

      spyOn(console, 'log');
      service['processTriggers']();
      tick();

      expect(apiService.executeCustomTriggerRoutine).not.toHaveBeenCalled();
      expect(console.log).toHaveBeenCalledWith('Processing pastRoute with timedifference', jasmine.any(Number));
    }));
  });

  describe('handleTriggerDisplayText', () => {
    it('should handle audio playback and add chat message', fakeAsync(() => {
      const mockBlob = new Blob(['audio data'], { type: 'audio/mpeg' });
      apiService.getTtsAudioFile.and.returnValue(of(mockBlob));

      spyOn(URL, 'createObjectURL').and.returnValue('mockAudioUrl');
      spyOn(URL, 'revokeObjectURL');
      const mockAudio = jasmine.createSpyObj('Audio', ['play']);
      spyOn(window as any, 'Audio').and.returnValue(mockAudio);

      service['handleTriggerDisplayText']('Test Message', 0);
      tick();

      expect(chatService.addMessage).toHaveBeenCalledWith('Test Message', 'chatgpt');
      expect(mockAudio.play).toHaveBeenCalled();
      expect(URL.createObjectURL).toHaveBeenCalledWith(mockBlob);

      // Simulate audio end
      mockAudio.onended();
      expect(URL.revokeObjectURL).toHaveBeenCalledWith('mockAudioUrl');
    }));

    it('should handle errors during audio playback', fakeAsync(() => {
      apiService.getTtsAudioFile.and.returnValue(throwError(() => new Error('Audio Error')));
      spyOn(console, 'error');

      service['handleTriggerDisplayText']('Test Message', 0);
      tick();

      expect(console.error).toHaveBeenCalledWith('Error occurred:', jasmine.any(Error));
    }));
  });

  describe('clearAllTriggers', () => {
    it('should clear all timeouts', () => {
      const mockTimeoutRef = setTimeout(() => {}, 1000);
      service['timoutReferenceMap'] = { testRoute: mockTimeoutRef };

      spyOn(window, 'clearTimeout');
      service['clearAllTriggers']();

      expect(clearTimeout).toHaveBeenCalledWith(mockTimeoutRef);
      expect(service['timoutReferenceMap']).toEqual({});
    });
  });
});
