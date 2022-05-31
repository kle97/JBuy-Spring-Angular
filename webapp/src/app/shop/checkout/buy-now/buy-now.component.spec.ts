import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyNowComponent } from './buy-now.component';

describe('BuyNowComponent', () => {
  let component: BuyNowComponent;
  let fixture: ComponentFixture<BuyNowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuyNowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BuyNowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
