import { TestBed } from '@angular/core/testing';
import { AudioRecorderService } from './audio-recorder.service';

describe('AudioRecorderService', () => {
  let service: AudioRecorderService;
  let mockMediaRecorder: any;

  beforeAll(() => {
    const mockMediaStream = new MediaStream();
    spyOn(navigator.mediaDevices, 'getUserMedia').and.returnValue(Promise.resolve(mockMediaStream));

    mockMediaRecorder = jasmine.createSpyObj('MediaRecorder', ['start', 'stop'], {
      ondataavailable: () => {},
      onstop: () => {},
      onerror: () => {}
    });

    spyOn(window as any, 'MediaRecorder').and.returnValue(mockMediaRecorder);
  });

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AudioRecorderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start recording', async () => {
    await service.startRecording();

    expect(navigator.mediaDevices.getUserMedia).toHaveBeenCalledWith({ audio: true });
    expect(mockMediaRecorder.start).toHaveBeenCalled();
  });

  it('should reject the promise if there is an error during recording', done => {
    service.stopRecording().catch(error => {
      expect(error).toBeDefined();
      done();
    });

    mockMediaRecorder.onerror({ name: 'error', message: 'Test error' });
  });

});
