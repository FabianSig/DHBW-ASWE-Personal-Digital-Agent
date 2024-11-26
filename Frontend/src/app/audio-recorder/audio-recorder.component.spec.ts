import { TestBed } from '@angular/core/testing';
import { AudioRecorderService } from '../services/audio-recorder.service';

describe('AudioRecorderService', () => {
  let service: AudioRecorderService;
  let mediaRecorderMock: any;
  let mediaStreamMock: any;

  beforeEach(() => {
    mediaStreamMock = new MediaStream();
    mediaRecorderMock = {
      start: jasmine.createSpy('start'),
      stop: jasmine.createSpy('stop'),
      ondataavailable: null,
      onstop: null,
    };

    spyOn(window.navigator.mediaDevices, 'getUserMedia').and.returnValue(Promise.resolve(mediaStreamMock));
    spyOn(window, 'MediaRecorder').and.returnValue(mediaRecorderMock);

    TestBed.configureTestingModule({});
    service = TestBed.inject(AudioRecorderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start recording', async () => {
    await service.startRecording();
    expect(mediaRecorderMock.start).toHaveBeenCalled();
  });

  it('should stop recording and resolve with audio Blob', async () => {
    const mockBlob = new Blob(['audio'], { type: 'audio/webm' });

    // Simulate MediaRecorder behavior for stop()
    mediaRecorderMock.stop.and.callFake(() => {
      if (mediaRecorderMock.ondataavailable) {
        mediaRecorderMock.ondataavailable({ data: mockBlob });
      }
      if (mediaRecorderMock.onstop) {
        mediaRecorderMock.onstop();
      }
    });

    // Start recording and then stop it
    await service.startRecording();
    const result = await service.stopRecording();

    // Assertions
    expect(mediaRecorderMock.stop).toHaveBeenCalled();
    expect(result).toEqual(mockBlob);
  });

  it('should handle error if MediaRecorder is not initialized', async () => {
    service['mediaRecorder'] = null as unknown as MediaRecorder; // Simulate uninitialized state

    try {
      await service.stopRecording();
      fail('Expected error to be thrown');
    } catch (error) {
      expect(error).toBe('MediaRecorder is not initialized');
    }
  });
});
