import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountInfosTransaction } from '../_models/account';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private baseUrl = 'http://localhost:8080/transaction';

  constructor(private http: HttpClient) {}

  getTransactionsByUserId(userId: number): Observable<AccountInfosTransaction[]> {
    return this.http.get<AccountInfosTransaction[]>(`${this.baseUrl}/${userId}`);
  }
}