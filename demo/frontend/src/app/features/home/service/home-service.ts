import { Injectable } from "@angular/core";
import { BASE_API } from "../../../env";
import { HttpClient } from "@angular/common/http";
import { Todo, TODO_STATUS } from "../model/todo";
import { CreateTodoRequest } from "../model/createTodoRequest";

@Injectable({
  providedIn: "root",
})
export class HomeService {
  private readonly todoUrl = `${BASE_API}/todos`

  /**
   *
   */
  constructor(private httpClient: HttpClient) {
  }
  findTodos(userId: number) {
    return this.httpClient.get<Todo[]>(`${this.todoUrl}/user/${userId}`)
  }
  createTodo(request: CreateTodoRequest) {
    return this.httpClient.post(`${this.todoUrl}`, request)
  }
  deleteTodo(userId: number, todoId: number) {
    return this.httpClient.delete(`${this.todoUrl}/${userId}/${todoId}`)
  }
  changeTodoStatus(userId: number, todoId: number, status: TODO_STATUS) {
    return this.httpClient.put(`${this.todoUrl}`, { status, userId, todoId })
  }
}
