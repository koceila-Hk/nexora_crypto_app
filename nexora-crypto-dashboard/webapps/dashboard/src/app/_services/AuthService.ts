// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
// import { environment } from '../../environments/envionment';

// interface TokenResponse {
//   accessToken: string;
//   refreshToken: string;
// }

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {


//   constructor(private http: HttpClient) {}


//   refreshToken(): Observable<TokenResponse> {
//     const refreshToken = localStorage.getItem('refresh_token'); 
//     // console.log('refreshToken :' , refreshToken);
//     return this.http.post<TokenResponse>(environment.apiUrl + `/auth/refresh-token`, {
//       refreshToken
//     });
//   }
// }


import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AccountInfosUser } from '../_models/account';
import { environment } from '../../environments/envionment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient)
  private _currentUser = signal<AccountInfosUser | null>(null)
  currentUser = this._currentUser.asReadonly()
  isConnected = computed(() => this.currentUser() !== null)

  login(credentials: AccountInfosUser): Observable<{
    user: AccountInfosUser
  }> {
    return this.http.post<{
      user: AccountInfosUser
    }>(environment.apiUrl + '/auth/login', credentials,{ withCredentials: true })
      .pipe(
        tap(response => {
          // Les deux tokens sont automatiquement stockés dans des cookies HTTP-only
          // Nous mettons à jour l'état de l'utilisateur connecté
          this._currentUser.set(response.user);
        })
      );
  }

  // Méthode pour rafraîchir les tokens. Utilisée par l'intercepteur HTTP
  revokeToken(): Observable<any> {
    return this.http.post<any>(environment.apiUrl + '/auth/revoke-token', {}, { withCredentials: true })
      .pipe(
        tap(response => {
          // Les nouveaux tokens sont automatiquement stockés dans des cookies HTTP-only
          console.log('Tokens refreshed successfully');
        })
      );
  }

  logout(): Observable<any> {
    return this.http.post<any>(environment.apiUrl + '/auth/logout', {}, { withCredentials: true })
      .pipe(
        tap(() => {
          // Le backend devrait supprimer les cookies
          this._currentUser.set(null);
        })
      );
  }
}