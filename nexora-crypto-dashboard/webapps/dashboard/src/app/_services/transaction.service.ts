import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountInfosTransaction } from '../_models/account';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/envionment';


@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private baseUrl = environment.apiUrl + '/transaction';

  constructor(private http: HttpClient) {}

  getTransactionsByUserId(userId: number): Observable<AccountInfosTransaction[]> {
    return this.http.get<AccountInfosTransaction[]>(`${this.baseUrl}/${userId}`);
  }
}