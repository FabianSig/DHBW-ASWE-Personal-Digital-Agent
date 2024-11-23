import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize title, day and time on init', () => {
    // @ts-ignore
    spyOn(component, 'updateDateTime').and.callThrough();
    component.ngOnInit();
    expect(component.title).toBe('Assistify AI');
    // @ts-ignore
    expect(component.updateDateTime).toHaveBeenCalled();
    expect(component.day).toMatch(/^\d{2}\.\d{2}\.\d{4}$/); // Matches date format
    expect(component.time).toMatch(/^\d{2}:\d{2}:\d{2}$/); // Matches time format
  });

  it('should update time and day every second', fakeAsync(() => {
    const initialDay = component.day;
    const initialTime = component.time;

    tick(1000);
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.day).toBe(initialDay); // Day should remain the same after one tick
    expect(component.time).not.toBe(initialTime); // Time should have updated

    tick(1000);
    // @ts-ignore
    expect(component.updateDateTime).toHaveBeenCalledTimes(2);
  }));

  it('should clear interval on destroy', () => {
    spyOn(window, 'clearInterval');
    component.ngOnDestroy();
    expect(clearInterval).toHaveBeenCalledWith(component['timerId']);
  });

  afterEach(() => {
    component.ngOnDestroy(); // Cleanup: Ensure the interval is cleared between tests
  });
});
