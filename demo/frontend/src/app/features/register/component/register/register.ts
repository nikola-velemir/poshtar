import { Component, signal } from "@angular/core";
import { RegisterService } from "../../service/register-service";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-register",
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: "./register.html",
  styleUrl: "./register.css",
})
export class RegisterComponent {

  errorMessage = signal("");
  isLoading = signal(false);
  isSuccess = signal(false);
  showPassword = signal(false);
  registerForm = new FormGroup({
    username: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    lastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
  })
  constructor(private registerService: RegisterService) {

  }
  togglePassword() {
    this.showPassword.update(v => !v)
  }
  onSubmit() {
    this.isLoading.set(true);
    this.errorMessage.set('');
    const { username, password, email, firstName, lastName } = this.registerForm.value;
    if (!username || !password || !email || !firstName || !lastName) {
      this.isLoading.set(false);
      return;
    }
    this.registerService.register({
      username,
      password,
      email,
      firstName,
      lastName
    }).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.isSuccess.set(true);
      },
      error: () => {
        this.errorMessage.set("Could not register");
        this.isLoading.set(false);
        this.isSuccess.set(false);
      }

    })
  }
}
