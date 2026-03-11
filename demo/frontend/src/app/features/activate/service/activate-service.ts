import { Injectable } from "@angular/core";
import { BASE_API } from "../../../env";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class ActivateService {
  private url = `${BASE_API}/users/activate`
  /**
   *
   */
  constructor(private httpClient: HttpClient) {

  }
  activate(username: string) {
    return this.httpClient.put(`${this.url}/${username}`, {})
  }
}
