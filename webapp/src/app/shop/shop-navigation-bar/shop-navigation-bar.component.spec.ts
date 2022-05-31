import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopNavigationBarComponent } from './shop-navigation-bar.component';

describe('ShopNavigationBarComponent', () => {
  let component: ShopNavigationBarComponent;
  let fixture: ComponentFixture<ShopNavigationBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShopNavigationBarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopNavigationBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
