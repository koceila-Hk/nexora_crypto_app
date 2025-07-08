import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { InfosCoin } from '../_models/account';
import { environment } from '../../environments/envionment';

@Injectable({
  providedIn: 'root'
})
export class CryptoService {

  private baseUrl = environment.apiUrl + '/crypto/details';
  private coins = [
    'bitcoin',
    // 'ethereum',
    // 'dogecoin',
    // 'binancecoin'
    ];

  constructor(private http: HttpClient) {}

  getAllCoinDetails(currency: string = 'eur'): Observable<InfosCoin[]> {
    const requests = this.coins.map(id =>
      this.http.get<InfosCoin>(`${this.baseUrl}/${id}?currency=${currency}`)
    );
    return forkJoin(requests);
  }
}