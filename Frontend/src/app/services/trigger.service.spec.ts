import { TestBed } from '@angular/core/testing';
import { TriggerService } from './trigger.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ApiService} from './api.service';


describe('TriggerService', () => {
  let service: TriggerService;
  let apiService: ApiService;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TriggerService, ApiService]
    });
    service = TestBed.inject(TriggerService);
    apiService = TestBed.inject(ApiService);

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get trigger data', () => {
    const date = '2023-01-01';
    const mockResponse = { trigger: 'trigger data' };

    apiService.getTriggerData(date).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
  });

  it('should clear all active triggers in clearAllTriggers', () => {
    // Mock timeouts
    const fakeTimeoutRef1 = setTimeout(() => {}, 1000);
    const fakeTimeoutRef2 = setTimeout(() => {}, 2000);

    // Populate the timoutReferenceMap with mock timeout references
    service['timoutReferenceMap'] = {
      '/routine1': fakeTimeoutRef1,
      '/routine2': fakeTimeoutRef2,
    };

    spyOn(window, 'clearTimeout').and.callThrough(); // Spy on the global clearTimeout function

    service['clearAllTriggers'](); // Call the method to test

    expect(clearTimeout).toHaveBeenCalledTimes(2); // Verify that clearTimeout was called for each timeout
    expect(service['timoutReferenceMap']).toEqual({}); // Ensure the map is cleared
  });
});
