import { Injectable } from "@angular/core";
import { PORT } from "../../env";

@Injectable({
  providedIn: "root",
})
export class ThemeService {

  readonly theme: 'spring' | 'guice' =
     PORT === '8080' ? 'spring' : 'guice';
}
