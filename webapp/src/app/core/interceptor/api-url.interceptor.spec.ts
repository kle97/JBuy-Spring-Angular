import { TestBed } from "@angular/core/testing";

import { ApiUrlInterceptor } from "./api-url.interceptor";

describe("ApiUrlInterceptor", () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      ApiUrlInterceptor,
    ],
  }));

  it("should be created", () => {
    const interceptor: ApiUrlInterceptor = TestBed.inject(ApiUrlInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
