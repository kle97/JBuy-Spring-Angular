import { TestBed } from '@angular/core/testing';

import { GenericCrudService } from './generic-crud.service';

describe('GenericCrudService', () => {
  let service: GenericCrudService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GenericCrudService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
