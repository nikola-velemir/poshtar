import { ComponentFixture, TestBed } from "@angular/core/testing";

import { TodoCard } from "./todo-card";

describe("TodoCard", () => {
  let component: TodoCard;
  let fixture: ComponentFixture<TodoCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TodoCard],
    }).compileComponents();

    fixture = TestBed.createComponent(TodoCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
