import { NgIf } from "@angular/common";
import { Component } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { LoginService } from "../../service/loginService";

@Component({
  selector: "app-login",
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: "./login.html",
  styleUrl: "./login.css",
})
export class LoginComponent {
  isLoading = false;
  showPassword = false;
  errorMessage = '';
  loginForm = new FormGroup({
    username: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
  })
  constructor(private service: LoginService, private router: Router) {

  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }


  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const { username, password } = this.loginForm.value;
    if (!username || !password) return;
    this.service.login({ username, password }).subscribe(
      {
        next: (response) => {
          this.router.navigate(["home"],
            { state: {user:response} }
          )
        }

      }
    )
  }
}
