import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuCarouselComponent } from './menu-carousel.component';

describe('MenuCarouselComponent', () => {
  let component: MenuCarouselComponent;
  let fixture: ComponentFixture<MenuCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MenuCarouselComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MenuCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
