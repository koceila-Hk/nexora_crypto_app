import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/envionment';

interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private http: HttpClient) {}


  refreshToken(): Observable<TokenResponse> {
    const refreshToken = localStorage.getItem('refresh_token'); 
    // console.log('refreshToken :' , refreshToken);
    return this.http.post<TokenResponse>(environment.apiUrl + `/auth/refresh-token`, {
      refreshToken
    });
  }
}
