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
  // logoUrl: string;
  variationPercentage: number;
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
