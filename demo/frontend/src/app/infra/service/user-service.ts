import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { User } from "../model/user";

const USER_KEY = "user";
@Injectable({
  providedIn: "root",
})
export class UserService {
  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable()

  constructor() {
    this.loadUser();
  }
  private loadUser() {
    const userString = localStorage.getItem(USER_KEY)
    const user : User = userString ? JSON.parse(userString) : null;
    this.userSubject.next(user)
  }
  setUser(user: User) {
    this.userSubject.next(user);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }
  clearUser(){
    this.userSubject.next(null);
    localStorage.removeItem(USER_KEY);
  }

}
