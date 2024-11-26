import { TestBed } from '@angular/core/testing';
import { AudioRecorderService } from './audio-recorder.service';

describe('AudioRecorderService', () => {
  let service: AudioRecorderService;
  let mockMediaRecorder: any;

  beforeAll(() => {
    const mockMediaStream = new MediaStream();
    spyOn(navigator.mediaDevices, 'getUserMedia').and.returnValue(Promise.resolve(mockMediaStream));

    mockMediaRecorder = jasmine.createSpyObj('MediaRecorder', ['start', 'stop'], {
      ondataavailable: null,
      onstop: null,
      onerror: null
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

  it('should stop recording and resolve with audio Blob', async () => {
    const mockBlobData = new Blob(['audio data'], { type: 'audio/ogg' });
    let resolveBlob: Blob | undefined;

    mockMediaRecorder.ondataavailable = (event: any) => {
      event.data = mockBlobData;
    };

    mockMediaRecorder.onstop = () => {
      resolveBlob = mockBlobData;
    };

    service.stopRecording().then(blob => {
      expect(blob).toEqual(mockBlobData);
    });

    expect(mockMediaRecorder.stop).toHaveBeenCalled();

    // Simulate the stop event
    mockMediaRecorder.onstop();

    // Wait for the promise to resolve
    await new Promise(resolve => setTimeout(resolve, 10));

    expect(resolveBlob).toBeDefined();
    expect(resolveBlob instanceof Blob).toBeTrue();
  });

  it('should reject the promise if there is an error during recording', done => {
    service.stopRecording().catch(error => {
      expect(error).toBeDefined();
      done();
    });

    mockMediaRecorder.onerror({ name: 'error', message: 'Test error' });
  });

  it('should handle error if MediaRecorder is not initialized', async () => {
    let errorThrown: any;

    await service.stopRecording().catch(error => {
      errorThrown = error;
    });

    expect(errorThrown).toBeDefined();
    expect(errorThrown instanceof TypeError).toBeTrue();
    expect(errorThrown.message).toBe(new TypeError('MediaRecorder is not initialized.').message);
  });
});
