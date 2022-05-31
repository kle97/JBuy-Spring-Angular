import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigationBarCartComponent } from './navigation-bar-cart.component';

describe('NavigationBarCartComponent', () => {
  let component: NavigationBarCartComponent;
  let fixture: ComponentFixture<NavigationBarCartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NavigationBarCartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NavigationBarCartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
