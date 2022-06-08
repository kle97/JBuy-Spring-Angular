import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrollUpButtonComponent } from './scroll-up-button.component';

describe('ScrollUpButtonComponent', () => {
  let component: ScrollUpButtonComponent;
  let fixture: ComponentFixture<ScrollUpButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScrollUpButtonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScrollUpButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
