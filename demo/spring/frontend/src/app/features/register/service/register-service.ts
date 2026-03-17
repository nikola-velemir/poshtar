import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { RegisterRequest } from "../model/registerRequest";
import { BASE_API } from "../../../env";

@Injectable({
  providedIn: "root",
})
export class RegisterService {

  constructor(private httpClient: HttpClient) {

  }
  register(request: RegisterRequest) {
    return this.httpClient.post(`${BASE_API}/users/register`, request)
  }
}
