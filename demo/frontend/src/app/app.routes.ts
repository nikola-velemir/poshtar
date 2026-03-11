import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/component/login/login';
import { HomeComponent } from './features/home/component/home/home';

export const routes: Routes = [
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: "home",
        component: HomeComponent
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    }
];