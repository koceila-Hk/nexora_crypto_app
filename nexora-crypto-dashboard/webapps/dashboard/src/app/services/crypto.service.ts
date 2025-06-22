import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';

export interface CoinDetails {
  cryptoName: string;
  icon: string;
  currentPrice: number;
  priceChangePercentage: number;
}

export interface WalletDetail {
  cryptoName: string;
  quantity: number;
  // logoCrypto: string;
  variationPercentage: number;
}

export interface User {
  // id: number;
  // username: string;
  balance: number;
  // ajoute d'autres champs si nécessaire
}

export interface Transactions {
  cryptoName: string;
  quantity: number;
  unitPrice: number;
  type: string;
  totalAmount: number;
  dateTransaction: string;
}

@Injectable({
  providedIn: 'root'
})
export class CryptoService {

  private baseUrl = 'http://localhost:8080/crypto/details';
  private coins = [
    'bitcoin',
    // 'ethereum',
    // 'dogecoin',
    // 'binancecoin'
    ];

  constructor(private http: HttpClient) {}

  getAllCoinDetails(currency: string = 'eur'): Observable<CoinDetails[]> {
    const requests = this.coins.map(id =>
      this.http.get<CoinDetails>(`${this.baseUrl}/${id}?currency=${currency}`)
    );
    return forkJoin(requests);
  }
}

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private baseUrl = 'http://localhost:8080/wallets';

  constructor(private http: HttpClient) {}

  getWalletsWithVariation(userId: number): Observable<WalletDetail[]> {
    return this.http.get<WalletDetail[]>(`${this.baseUrl}/variation/${userId}`);
  }
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = 'http://localhost:8080/users/balance';

  constructor(private http: HttpClient) {}

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private baseUrl = 'http://localhost:8080/transaction';

  constructor(private http: HttpClient) {}

  getTransactionsByUserId(userId: number): Observable<Transactions[]> {
    return this.http.get<Transactions[]>(`${this.baseUrl}/${userId}`);
  }
}