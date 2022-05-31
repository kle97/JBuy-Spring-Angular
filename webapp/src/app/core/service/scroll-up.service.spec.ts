import { TestBed } from '@angular/core/testing';

import { ScrollUpService } from './scroll-up.service';

describe('ScrollUpService', () => {
  let service: ScrollUpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScrollUpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
