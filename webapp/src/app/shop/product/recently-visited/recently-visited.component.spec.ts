import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentlyVisitedComponent } from './recently-visited.component';

describe('RecentlyVisitedComponent', () => {
  let component: RecentlyVisitedComponent;
  let fixture: ComponentFixture<RecentlyVisitedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecentlyVisitedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecentlyVisitedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
