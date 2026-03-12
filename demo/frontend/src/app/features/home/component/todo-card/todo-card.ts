import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Todo, TODO_STATUS } from "../../model/todo";
import { NgIf } from "@angular/common";

@Component({
  selector: "app-todo-card",
  imports: [NgIf],
  templateUrl: "./todo-card.html",
  styleUrl: "./todo-card.css",
})
export class TodoCard {

  @Input() todo!: Todo;

  @Output() emitDelete = new EventEmitter<number>();

  @Output() emitChangeStatus = new EventEmitter<{ id: number, status: TODO_STATUS }>();
  deleteTodo() {
    this.emitDelete.emit(this.todo?.id)
  }
  changeStatus() {
    if (!this.todo) return;
    this.emitChangeStatus.emit({ id: this.todo.id, status: this.todo.status })
  }
}
