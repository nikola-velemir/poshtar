import { Component } from "@angular/core";
import { RegisterService } from "../../service/register-service";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-register",
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: "./register.html",
  styleUrl: "./register.css",
})
export class RegisterComponent {

  errorMessage = '';
  isLoading = false;

  showPassword: boolean = false;
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
    this.showPassword = !this.showPassword
  }
  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    const { username, password, email, firstName, lastName } = this.registerForm.value;
    if (!username || !password || !email || !firstName || !lastName)
      return;
    this.registerService.register({
      username,
      password,
      email,
      firstName,
      lastName
    }).subscribe({
      next :()=>{
        this.isLoading = false;
      },
      error : ()=> this.isLoading = false
    })
  }
}
