import { Routes } from '@angular/router';
import { Auth } from './components/auth/auth';

export const routes: Routes = [
    {
        path: "",
        redirectTo: "/auth",
        pathMatch: "full"
    },{
        path: "auth",
        component: Auth,
    },{
        path: "**",
        redirectTo: "/auth"
    }
];
