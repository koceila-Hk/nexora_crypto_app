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
import { environment } from '../../environments/envionment';
import { AuthService } from '../_services/AuthService';

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

  constructor(private http: HttpClient,
    private router: Router,
    // private tokenStorage: TokenStorageService,
    private authService: AuthService) { }

  onCoinSelected(coin: InfosCoin): void {
    this.selectedCoin = coin;
    this.calculateConversion();
  }

  calculateConversion(): void {
    if (this.selectedCoin && this.amountInput > 0) {
      const price = this.selectedCoin.currentPrice;
      if (this.mode === 'buy') {
        this.resultAmount = this.amountInput / price;
      } else {
        this.resultAmount = this.amountInput * price;
      }
      this.errorMessage = '';
    } else {
      this.resultAmount = 0;
    }
  }

  submit(): void {
    if (!this.selectedCoin) {
      this.errorMessage = 'Veuillez sélectionner une cryptomonnaie.';
      return;
    }

    // const userId = this.tokenStorage.getUserIdFromToken();
    const userId = this.authService.currentUser()?.id;
    // console.log(userId);
    if (userId != null) {
      if (this.selectedCoin && this.amountInput > 0) {
        const transaction = {
          userId,
          cryptoName: this.selectedCoin.cryptoName,
          quantity: this.amountInput,
          unitPrice: this.selectedCoin.currentPrice,
          totalAmount: this.resultAmount,
          type: this.mode.toUpperCase()
        };

        const apiUrl = environment.apiUrl + `/transaction/${this.mode}`;

        this.http.post(apiUrl, transaction, { withCredentials: true }).subscribe({
          next: (res) => {
            this.errorMessage = '';
            this.router.navigate(['/home-auth']);
          },
          error: (err) => {
            console.error(`Erreur lors de la transaction :`, err);
            this.errorMessage = err.error?.message || 'Erreur inconnue';
          }
        });
      }
    }
  }
}
