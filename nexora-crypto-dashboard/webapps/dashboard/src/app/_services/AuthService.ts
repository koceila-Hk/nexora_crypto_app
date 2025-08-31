import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap, tap, using } from 'rxjs';
import { AccountInfosUser } from '../_models/account';
import { environment } from '../../environments/envionment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient)
   _currentUser = signal<AccountInfosUser | null>(null)
  currentUser = this._currentUser.asReadonly()
  isConnected = computed(() => this.currentUser() !== null)

  login(credentials: AccountInfosUser): Observable<{user: AccountInfosUser}> {
    return this.http.post<{user: AccountInfosUser}>(environment.apiUrl + '/auth/login', credentials, { withCredentials: true })
      .pipe(
        tap(response => {
          // Les deux tokens sont automatiquement stockés dans des cookies HTTP-only
          console.log('response :', response);
          this._currentUser.set(response.user);
        })
      );
  }

  // Méthode pour rafraîchir les tokens. Utilisée par l'intercepteur HTTP
refreshToken(): Observable<AccountInfosUser> {
  return this.http.post<any>(environment.apiUrl + '/auth/refresh-token', {}, { withCredentials: true }).pipe(
    switchMap(() => this.getCurrentUser()), // récupère utilisateur à jour
    tap(user => {
      this._currentUser.set(user);
      console.log('Tokens refreshed and user updated');
    })
  );
}


  logout(): Observable<any> {
    return this.http.post<any>(environment.apiUrl + '/auth/logout', {}, { withCredentials: true })
      .pipe(
        tap(() => {
          this._currentUser.set(null);
        })
      );
  }

  getCurrentUser(): Observable<AccountInfosUser> {
    return this.http.get<AccountInfosUser>(environment.apiUrl + '/users/me', { withCredentials: true })
      .pipe(
        tap(user => this._currentUser.set(user))
      );
  }

    // --- MOT DE PASSE OUBLIE ---
  forgotPassword(email: string): Observable<any> {
    return this.http.post(environment.apiUrl + `/auth/forgot-password?email=${email}`, {}, { withCredentials: true });
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(environment.apiUrl + '/auth/reset-password', { token, newPassword }, { withCredentials: true });
  }
}