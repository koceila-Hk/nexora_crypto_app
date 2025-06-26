import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountInfosUser } from '../_models/account';


@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = 'http://localhost:8080/users/balance';

  constructor(private http: HttpClient) {}

  getUserById(id: number): Observable<AccountInfosUser> {
    return this.http.get<AccountInfosUser>(`${this.baseUrl}/${id}`);
  }
}