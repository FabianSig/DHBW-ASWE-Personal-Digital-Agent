import { TestBed } from '@angular/core/testing';
import { TriggerService } from './trigger.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';


describe('TriggerService', () => {
  let service: TriggerService;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TriggerService]
    });
    service = TestBed.inject(TriggerService);

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


});
