import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountInfosTransaction } from '../_models/account';


// export interface Transactions {
//   cryptoName: string;
//   quantity: number;
//   unitPrice: number;
//   type: string;
//   totalAmount: number;
//   dateTransaction: string;
// }

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