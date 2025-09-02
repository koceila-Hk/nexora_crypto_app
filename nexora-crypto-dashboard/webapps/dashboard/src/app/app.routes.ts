import { Routes } from '@angular/router';
import { LoginComponent } from './_authentification/login/login.component';
import { RegisterComponent } from './_authentification/register/register.component'; 
import { VerifyComponent } from './_authentification/verify/verify.component';
import { AuthGuard } from './_utils/auth-guard';
import { HomeNotAuthComponent } from './home-not-auth/home-not-auth.component';
import { HomeAuthComponent } from './home-auth/home-auth.component';
import { MarketsComponent } from './markets/markets.component';
import { CryptoDetailsComponent } from './_commons/crypto-details/crypto-details.component';
import { DashboardBuySellCoinComponent } from './dashboard-buy-sell-coin/dashboard-buy-sell-coin.component';
import { ChartDetailComponent } from './chart-detail/chart-detail.component';
import { TermsOfUseComponent } from './_legal/terms/terms.use.component';
import { PrivacyPolicyComponent } from './_legal/privacy-policy/privacy.policy.component';
import { ContactComponent } from './_legal/contact/contact.component';
import { CookiesComponent } from './_legal/cookies/cookies.component';
import { MentionsLegalesComponent } from './_legal/mentions-legales/mentions-legales.component';
import { ResetPasswordComponent } from './_authentification/reset-password/reset-password.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent  },
  { path: 'verify', component: VerifyComponent  },
  { path: 'login', component: LoginComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'home-not-auth', component: HomeNotAuthComponent},
  { path: 'home-auth', component: HomeAuthComponent},
  { path: 'markets', component: MarketsComponent},
  { path: 'buy&sell-coin', component: DashboardBuySellCoinComponent},
  { path: 'crypto-details', component: CryptoDetailsComponent},
  { path: 'crypto-chart/:id', component: ChartDetailComponent },
  { path: 'terms', component: TermsOfUseComponent},
  { path: 'privacy', component: PrivacyPolicyComponent},
  { path: 'contact', component: ContactComponent },
  { path: 'cookies', component: CookiesComponent },
  { path: 'mentions-legales', component: MentionsLegalesComponent },
  // { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'home-not-auth', pathMatch: 'full' }, 
]; 