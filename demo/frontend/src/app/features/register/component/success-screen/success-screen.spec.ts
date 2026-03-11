import { ComponentFixture, TestBed } from "@angular/core/testing";

import { SuccessScreen } from "./success-screen";

describe("SuccessScreen", () => {
  let component: SuccessScreen;
  let fixture: ComponentFixture<SuccessScreen>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SuccessScreen],
    }).compileComponents();

    fixture = TestBed.createComponent(SuccessScreen);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
