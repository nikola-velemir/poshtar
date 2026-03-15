import { NgIf } from "@angular/common";
import { ChangeDetectorRef, Component, signal } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { LoginService } from "../../service/loginService";
import { UserService } from "../../../../infra/service/user-service";
import { single } from "rxjs";

@Component({
  selector: "app-login",
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: "./login.html",
  styleUrl: "./login.css",
  standalone: true,
})
export class LoginComponent {
  isLoading = signal(false);
  showPassword = signal(false);
  errorMessage = signal('');
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
  constructor(private service: LoginService, private userService: UserService, private router: Router) {

  }

  togglePassword(): void {
    this.showPassword.update(v => !v)
  }

  navigateToRegister(){
    console.log("to register")
    this.router.navigate(['register'])
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    const { username, password } = this.loginForm.value;
    if (!username || !password) {
      this.isLoading.set(false);
      return;
    }
    this.service.login({ username, password }).subscribe(
      {
        next: (response) => {
          this.userService.setUser({
            ...response
          });
          this.isLoading.set(false);
          this.router.navigate(["home"]);
        },
        error: () => {
          console.log("AAAA")
          this.isLoading.set(false);
          this.errorMessage.set("Could not log in!");
        },

      }
    )
  }
}
