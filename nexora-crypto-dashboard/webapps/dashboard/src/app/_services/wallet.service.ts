import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountInfosWallet } from '../_models/account';
import { environment } from '../../environments/envionment';


@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private baseUrl = environment.apiUrl + '/wallets';

  constructor(private http: HttpClient) { }

  getWalletsWithVariation(userId: number): Observable<AccountInfosWallet[]> {
    return this.http.get<AccountInfosWallet[]>(`${this.baseUrl}/variation/${userId}`);
  }
}
