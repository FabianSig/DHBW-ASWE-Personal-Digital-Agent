import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AudioRecorderComponent } from './audio-recorder.component';
import { AudioRecorderService } from '../services/audio-recorder.service';
import { ApiService } from '../services/api.service';
import { of } from 'rxjs';
import { AudioResponse } from '../interfaces/audio-response';

describe('AudioRecorderComponent', () => {
  let component: AudioRecorderComponent;
  let fixture: ComponentFixture<AudioRecorderComponent>;
  let audioRecorderService: jasmine.SpyObj<AudioRecorderService>;
  let apiService: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    const audioRecorderServiceSpy = jasmine.createSpyObj('AudioRecorderService', ['startRecording', 'stopRecording']);
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['getAudioData']);

    await TestBed.configureTestingModule({
      declarations: [AudioRecorderComponent],
      providers: [
        { provide: AudioRecorderService, useValue: audioRecorderServiceSpy },
        { provide: ApiService, useValue: apiServiceSpy }
      ]
    }).compileComponents();

    audioRecorderService = TestBed.inject(AudioRecorderService) as jasmine.SpyObj<AudioRecorderService>;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;

    fixture = TestBed.createComponent(AudioRecorderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should start recording', () => {
    component.startRecording();
    expect(component.isRecording).toBeTrue();
    expect(audioRecorderService.startRecording).toHaveBeenCalled();
  });

  it('should stop recording and send audio', async () => {
    const mockAudioBlob = new Blob();
    audioRecorderService.stopRecording.and.returnValue(Promise.resolve(mockAudioBlob));

    spyOn(component['audioResponseEmitter'], 'emit');
    const mockAudioResponse: AudioResponse = { text: 'Hello from GPT' };
    apiService.getAudioData.and.returnValue(of(mockAudioResponse));

    await component.stopRecording();
    expect(component.isRecording).toBeFalse();
    expect(component.audioFile).toBe(mockAudioBlob);
    expect(apiService.getAudioData).toHaveBeenCalledWith(mockAudioBlob);
    expect(component['audioResponseEmitter'].emit).toHaveBeenCalledWith(mockAudioResponse);
  });

  it('should handle error if recording ends with failure', async () => {
    const consoleErrorSpy = spyOn(console, 'error');
    audioRecorderService.stopRecording.and.returnValue(Promise.reject('some error'));

    await component.stopRecording();
    expect(consoleErrorSpy).toHaveBeenCalledWith('Error stopping recording:', 'some error');
  });

  it('should not send audio if no file is available', () => {
    const consoleErrorSpy = spyOn(console, 'error');

    component['sendAudioToChatGPT']();
    expect(consoleErrorSpy).toHaveBeenCalledWith('No audio file available');
  });

  it('should handle error from ApiService', () => {
    const consoleErrorSpy = spyOn(console, 'error');
    component.audioFile = new Blob();
    apiService.getAudioData.and.returnValue(of({}).pipe(() => { throw 'API error'; }));

    component['sendAudioToChatGPT']();
    expect(consoleErrorSpy).toHaveBeenCalledWith('Error occurred:', 'API error');
  });
});
