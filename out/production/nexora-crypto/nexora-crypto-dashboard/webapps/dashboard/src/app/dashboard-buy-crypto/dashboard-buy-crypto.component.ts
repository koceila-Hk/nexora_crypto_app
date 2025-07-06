import { Component } from '@angular/core';
import { HeaderComponent } from "../_commons/header/header.component";
import { FooterComponent } from "../_commons/footer/footer.component";
import { CommonModule } from '@angular/common';
import { CryptoDetailsComponent } from '../_commons/crypto-details/crypto-details.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { TokenStorageService } from '../_services/tokenStorageService';
import { InfosCoin } from '../_models/account';

@Component({
  selector: 'app-markets',
  standalone: true,
  imports: [
    HeaderComponent,
    FooterComponent,
    CommonModule,
    CryptoDetailsComponent,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './dashboard-buy-crypto.component.html',
  styleUrl: './dashboard-buy-crypto.component.css'
})
export class DashboardBuyCryptoComponent {
  selectedCoin: InfosCoin | null = null;
  mode: 'buy' | 'sell' = 'buy'; 
  amountInput: number = 0;
  resultAmount: number = 0;
  errorMessage: string = '';

  constructor(private http: HttpClient, private router: Router, private tokenStorage: TokenStorageService) {}

  onCoinSelected(coin: InfosCoin): void {
    this.selectedCoin = coin;
    this.calculateConversion();
  }

  calculateConversion(): void {
    if (this.selectedCoin && this.amountInput > 0) {
      const price = this.selectedCoin.currentPrice;

      this.resultAmount = this.mode === 'buy'
        ? this.amountInput / price               
        : this.amountInput * price;             
    } else {
      this.resultAmount = 0;
    }
  }

  submit(): void {
  //   const userId = this.tokenStorage.getUserIdFromToken();
  //   console.log(userId);
  //     if (!userId) {
  //   this.errorMessage = 'Utilisateur non authentifié';
  //   return;
  // }
    if (this.selectedCoin && this.amountInput > 0) {
      const transaction = {
        userId: 3,
        cryptoName: this.selectedCoin.cryptoName,
        quantity: this.mode === 'buy' ? this.resultAmount : this.amountInput,
        unitPrice: this.selectedCoin.currentPrice,
        totalAmount: this.mode === 'buy' ? this.amountInput : this.resultAmount,
        type: this.mode.toUpperCase()
      };

      const apiUrl = `http://localhost:8080/transaction/${this.mode}`;

      this.http.post(apiUrl, transaction).subscribe({
        next: (res) => {
          console.log(`${this.mode.toUpperCase()} réussi`, res);
          this.errorMessage = '';
          // Redirection
          // this.router.navigate(['/transactions']);
        },
        error: (err) => {
          console.error(`Erreur lors de la transaction :`, err);
          this.errorMessage = err.error?.message || 'Erreur inconnue';
        }
      });
    }
  }
}
