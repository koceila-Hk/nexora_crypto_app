import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountInfosWallet } from '../_models/account';


@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private baseUrl = 'http://localhost:8080/wallets';

  constructor(private http: HttpClient) {}

  getWalletsWithVariation(userId: number): Observable<AccountInfosWallet[]> {
    return this.http.get<AccountInfosWallet[]>(`${this.baseUrl}/variation/${userId}`);
  }
}
