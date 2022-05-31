import { TestBed } from '@angular/core/testing';

import { ErrorNotificationService } from './error-notification.service';

describe('ErrorNotificationService', () => {
  let service: ErrorNotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ErrorNotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
