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

  it('should stop recording and resolve with audio Blob', async () => {
    const mockBlobData = new Blob(['audio data'], { type: 'audio/ogg' });
    let resolveBlob: Blob | undefined;

    spyOn(mockMediaRecorder, 'stop').and.callFake(() => {
      setTimeout(() => {
        (mockMediaRecorder.ondataavailable as any)({ data: mockBlobData });
        mockMediaRecorder.onstop();
      }, 0);
    });

    service.stopRecording().then(blob => {
      resolveBlob = blob;
    });

    expect(mockMediaRecorder.stop).toHaveBeenCalled();

    await new Promise(resolve => setTimeout(resolve, 10));

    expect(resolveBlob).toBeDefined();
    expect(resolveBlob instanceof Blob).toBeTrue();
    expect(resolveBlob).toEqual(mockBlobData);
  });

  it('should reject the promise if there is an error during recording', done => {
    let errorThrown: any;

    service.stopRecording().catch(error => {
      errorThrown = error;
      expect(errorThrown).toBeDefined();
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
    expect(errorThrown.message).toBe('MediaRecorder is not initialized.');
  });
});
