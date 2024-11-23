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
    let resolveBlob: Blob | undefined;

    // Simulate mediaRecorder ondataavailable and onstop events
    (mockMediaRecorder.ondataavailable as any)({ data: new Blob(['audio data']) });

    service.stopRecording().then(blob => {
      resolveBlob = blob;
    });

    expect(mockMediaRecorder.stop).toHaveBeenCalled();

    setTimeout(() => mockMediaRecorder.onstop(), 0); // Simulate async onstop event
    setTimeout(() => {
      expect(resolveBlob).toBeDefined();
      expect(resolveBlob instanceof Blob).toBeTrue();
    }, 0);
  });

  it('should reject the promise if there is an error during recording', done => {
    let errorThrown: any;

    // Here, testing stopRecording() instead, as that's the method dealing with Promise handling
    service.stopRecording().catch(error => {
      errorThrown = error;
      expect(errorThrown).toBeDefined();
      done();
    });

    // Simulate an error
    mockMediaRecorder.onerror({ name: 'error', message: 'Test error' });
  });
});
