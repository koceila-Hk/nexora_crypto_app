import { Routes } from '@angular/router';
import { LoginComponent } from './_authentification/login/login.component';
import { RegisterComponent } from './_authentification/register/register.component'; 
import { VerifyComponent } from './_authentification/verify/verify.component';
import { DashboardComponent } from './app-dashboard-component/dashboard/dashboard.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent  },
  { path: 'verify', component: VerifyComponent  },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
]; 