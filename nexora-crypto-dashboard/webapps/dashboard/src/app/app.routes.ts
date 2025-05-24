import { Routes } from '@angular/router';
import { LoginComponent } from './_authentification/login/login.component';
import { RegisterComponent } from './_authentification/register/register.component'; 
import { VerifyComponent } from './_authentification/verify/verify.component';
import { DashboardComponent } from './app-dashboard-component/dashboard/dashboard.component';
import { AuthGuard } from './interceptors/auth-guard';
import { HomeNotAuthComponent } from './home-not-auth/home-not-auth.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent  },
  { path: 'verify', component: VerifyComponent  },
  { path: 'login', component: LoginComponent },
  { path: 'home-not-auth', component: HomeNotAuthComponent},
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'home-not-auth', pathMatch: 'full' }, 
]; 