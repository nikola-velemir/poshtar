import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { User } from "../model/user";

@Injectable({
  providedIn: "root",
})
export class UserService {
  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable()
  setUser(user:User){
    this.userSubject.next(user);
  }

}
