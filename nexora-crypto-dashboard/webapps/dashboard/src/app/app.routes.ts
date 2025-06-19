import { Routes } from '@angular/router';
import { LoginComponent } from './_authentification/login/login.component';
import { RegisterComponent } from './_authentification/register/register.component'; 
import { VerifyComponent } from './_authentification/verify/verify.component';
import { DashboardComponent } from './app-dashboard-component/dashboard/dashboard.component';
import { AuthGuard } from './interceptors/auth-guard';
import { HomeNotAuthComponent } from './home-not-auth/home-not-auth.component';
import { MarketsComponent } from './markets/markets.component';
import { CryptoDetailsComponent } from './common/crypto-details/crypto-details.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent  },
  { path: 'verify', component: VerifyComponent  },
  { path: 'login', component: LoginComponent },
  { path: 'home-not-auth', component: HomeNotAuthComponent},
  { path: 'markets', component: MarketsComponent},
  { path: 'crypto-details', component: CryptoDetailsComponent},
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'home-not-auth', pathMatch: 'full' }, 
]; 