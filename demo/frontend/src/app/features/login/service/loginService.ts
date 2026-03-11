import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { LoginRequest } from "../model/request";
import { LoginResponse } from "../model/response";
import { BASE_API } from "../../../env";

@Injectable({
  providedIn: "root",
})
export class LoginService {
  private loginApiPath = `${BASE_API}/users/login`
  constructor(private client: HttpClient
  ) { }
  login(request: LoginRequest) {
    return this.client.post<LoginResponse>(
      this.loginApiPath,
      request
    )
  }
}
