import { Routes } from '@angular/router';
import { Auth } from './components/auth/auth';
import { MainLayout } from './layouts/main-layout/main-layout';
import { Chat } from './components/chat/chat';
import { authGuard, noAuthGuard } from './guards/auth-guard';

export const routes: Routes = [
    {
        path: "",
        redirectTo: "/auth",
        pathMatch: "full"
    },{
        path: "auth",
        component: Auth,
        canActivate: [noAuthGuard]
    },{
        path: "chat",
        component: MainLayout,
        canActivate: [authGuard],
        children: [
            {
                path: "",
                component: Chat
            },{
                path: ":id",
                component: Chat
            }
        ]
    },{
        path: "**",
        redirectTo: "/auth"
    }
];
