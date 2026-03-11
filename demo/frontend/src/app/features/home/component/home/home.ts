import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { LoginResponse } from "../../../login/model/response";
import { UserService } from "../../../../infra/service/user-service";
import { Observable, of } from "rxjs";
import { User } from "../../../../infra/model/user";
import { AsyncPipe,NgFor, NgIf } from '@angular/common'; // Import the pipe
@Component({
  selector: "app-home",
  imports: [NgIf,NgFor,AsyncPipe],
  templateUrl: "./home.html",
  styleUrl: "./home.css",
})
export class HomeComponent implements OnInit {
  user$ : Observable<User | null> = of(null);
  todayDate = '';
  greeting = '';
  constructor(private router: Router, private userService:UserService) {
  }
    ngOnInit(): void {
    this.user$ = this.userService.user$;
    const now = new Date();
    const h = now.getHours();
    this.greeting  = h < 12 ? 'morning' : h < 17 ? 'afternoon' : 'evening';
    this.todayDate = now.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' });
  }
}

