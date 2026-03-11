import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { LoginResponse } from "../../../login/model/response";
import { NgFor, NgIf } from "@angular/common";

@Component({
  selector: "app-home",
  imports: [NgIf,NgFor],
  templateUrl: "./home.html",
  styleUrl: "./home.css",
})
export class HomeComponent implements OnInit {
  userData: LoginResponse | null;
  todayDate = '';
  greeting = '';
  constructor(private router: Router) {
    this.userData = this.router.getCurrentNavigation()?.extras.state?.['user'];
  }
    ngOnInit(): void {
    const now = new Date();
    const h = now.getHours();
    this.greeting  = h < 12 ? 'morning' : h < 17 ? 'afternoon' : 'evening';
    this.todayDate = now.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' });
  }
}

