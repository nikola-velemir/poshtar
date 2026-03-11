import { TestBed } from "@angular/core/testing";

import { ActivateService } from "./activate-service";

describe("ActivateService", () => {
  let service: ActivateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ActivateService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});
