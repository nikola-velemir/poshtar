import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/component/login/login';
import { HomeComponent } from './features/home/component/home/home';
import { RegisterComponent } from './features/register/component/register/register';
import { ActivateComponent } from './features/activate/component/activate/activate';

export const routes: Routes = [
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path:"register",
        component:RegisterComponent
    },
    {
        path:'activate/:username',
        component:ActivateComponent
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