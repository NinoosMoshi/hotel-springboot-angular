import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { GuardService } from './service/guard.service';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },

    { path: 'profile', component: ProfileComponent, canActivate: [GuardService] },
    { path: 'edit-profile', component: EditProfileComponent, canActivate: [GuardService] },

    { path: '**', redirectTo: 'home' },
];
