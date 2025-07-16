import { Routes } from '@angular/router';
import { LoginComponent } from './_authentification/login/login.component';
import { RegisterComponent } from './_authentification/register/register.component'; 
import { VerifyComponent } from './_authentification/verify/verify.component';
import { DashboardComponent } from './app-dashboard-component/dashboard/dashboard.component';
import { AuthGuard } from './_utils/auth-guard';
import { HomeNotAuthComponent } from './home-not-auth/home-not-auth.component';
import { HomeAuthComponent } from './home-auth/home-auth.component';
import { MarketsComponent } from './markets/markets.component';
import { CryptoDetailsComponent } from './_commons/crypto-details/crypto-details.component';
import { DashboardBuyCryptoComponent } from './dashboard-buy-crypto/dashboard-buy-crypto.component';
import { ChartDetailComponent } from './chart-detail/chart-detail.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent  },
  { path: 'verify', component: VerifyComponent  },
  { path: 'login', component: LoginComponent },
  { path: 'home-not-auth', component: HomeNotAuthComponent},
  { path: 'home-auth', component: HomeAuthComponent},
  { path: 'markets', component: MarketsComponent},
  { path: 'buy&sell-crypto', component: DashboardBuyCryptoComponent},
  { path: 'crypto-details', component: CryptoDetailsComponent},
   { path: 'crypto-chart/:id', component: ChartDetailComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'home-not-auth', pathMatch: 'full' }, 
]; 