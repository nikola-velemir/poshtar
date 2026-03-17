import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { LoginResponse } from "../../../login/model/response";
import { UserService } from "../../../../infra/service/user-service";
import { BehaviorSubject, Observable, of, switchMap } from "rxjs";
import { User } from "../../../../infra/model/user";
import { AsyncPipe, CommonModule, NgFor, NgIf } from '@angular/common'; // Import the pipe
import { HomeService } from "../../service/home-service";
import { Todo, TODO_STATUS } from "../../model/todo";
import { FormsModule } from "@angular/forms";
import { TodoCard } from "../todo-card/todo-card";
import { ThemeService } from "../../../../infra/theme/theme-service";



@Component({

  selector: "app-home",
  imports: [NgIf, NgFor,CommonModule, AsyncPipe, FormsModule, TodoCard],
  templateUrl: "./home.html",
  styleUrl: "./home.css",
})
export class HomeComponent implements OnInit {
  logout() {
    this.userService.clearUser();
    this.router.navigate(["login"]);
  }

  user: User | null = null;
  user$: Observable<User | null> = of(null);
  todos$: Observable<Todo[]> = of([])
  private refresh$ = new BehaviorSubject<void>(undefined);

  todayDate = '';
  greeting = '';
  showAddModal: any;
  constructor(
    private themeService:ThemeService,
    private router: Router, 
    private userService: UserService,
     private homeService: HomeService) {
  }
  ngOnInit(): void {
    this.user$ = this.userService.user$;
    const now = new Date();
    const h = now.getHours();
    this.greeting = h < 12 ? 'morning' : h < 17 ? 'afternoon' : 'evening';
    this.todayDate = now.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' });

    this.todos$ = this.user$.pipe(switchMap((user) => {
      if (!user) return of([])
      this.user = user;
      return this.refresh$.pipe(
        switchMap(() => this.homeService.findTodos(user.id))
      );
    }));

  }
  get theme(){
    return this.themeService.theme;
  }
  changeStatus($event: { id: number, status: TODO_STATUS }) {
    if (!this.user) return;
    const { id: todoId, status: currentStatus } = $event;
    const newStatus = currentStatus == "COMPLETED" ? "PENDING" : "COMPLETED";
    this.homeService.changeTodoStatus(this.user.id, todoId, newStatus).subscribe({
      next: () => this.refresh$.next()
    });
  }
  trackById(index: number, todo: Todo) {
    return todo.id;
  }
  deleteTodo(todoId: number) {
    if (!this.user) return;
    this.homeService.deleteTodo(this.user.id, todoId).subscribe({
      next: () => {
        this.refresh$.next(); // triggers todos$ to re-fetch
        this.closeModal();
      }
    });
  }
  newTodo: Todo = { id: -1, title: "", description: "", status: "PENDING" };
  submitTodo() {
    if (!this.user) return

    this.homeService.createTodo({
      userId: this.user.id,
      title: this.newTodo.title,
      description: this.newTodo.description
    }).subscribe({
      next: () => {
        this.refresh$.next(); // triggers todos$ to re-fetch
        this.closeModal();
      }
    });
  }
  openModal() {
    this.showAddModal = true;
  }
  closeModal() {
    this.showAddModal = false;
  }
}

