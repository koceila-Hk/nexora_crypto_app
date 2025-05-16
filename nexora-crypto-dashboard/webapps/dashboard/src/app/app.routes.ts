import { Routes } from '@angular/router';
import { LoginComponent } from './_authentification/login/login.component';
import { RegisterComponent } from './_authentification/register/register.component'; 

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent  },
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
]; 