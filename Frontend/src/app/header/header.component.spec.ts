import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let originalDateNow: () => number;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;

    originalDateNow = Date.now;
    Date.now = () => new Date().getTime();

    fixture.detectChanges();
  });

  afterEach(() => {
    // Restore original Date.now after tests
    Date.now = originalDateNow;
    // @ts-ignore
    if (component.timerId) {
      // @ts-ignore
      clearInterval(component.timerId);
    }
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize title with "Assistify AI"', () => {
    expect(component.title).toBe('Assistify AI');
  });

  it('should set day and time correctly on initialization', () => {
    let dateTime = new Date()
    const expectedDay = `${String(dateTime.getDate()).padStart(2, '0')}.${String(dateTime.getMonth() + 1).padStart(2, '0')}.${dateTime.getFullYear()}`;
    const expectedTime = dateTime.toTimeString().split(' ')[0];

    expect(component.day).toBe(expectedDay);
    expect(component.time).toBe(expectedTime);
  });

  it('should update day and time every second', () => {
    const updateDateTimeSpy = spyOn<any>(component, 'updateDateTime').and.callThrough();

    jasmine.clock().install();
    component.ngOnInit();
    jasmine.clock().tick(1000);

    expect(updateDateTimeSpy).toHaveBeenCalledTimes(2); // Once in ngOnInit and once after tick
    jasmine.clock().uninstall();
  });

  it('should clear timer on destroy', () => {
    const clearIntervalSpy = spyOn(window, 'clearInterval').and.callThrough();
    component.ngOnDestroy();
    // @ts-ignore
    expect(clearIntervalSpy).toHaveBeenCalledWith(component.timerId);
  });

  it('should correctly format date and time in updateDateTime', () => {
    const dateTime = new Date();
    const expectedDay = `${String(dateTime.getDate()).padStart(2, '0')}.${String(dateTime.getMonth() + 1).padStart(2, '0')}.${dateTime.getFullYear()}`;
    const expectedTime = dateTime.toTimeString().split(' ')[0];

    component['updateDateTime'](); // Call the private method
    expect(component.day).toBe(expectedDay);
    expect(component.time).toBe(expectedTime);
  });
});
