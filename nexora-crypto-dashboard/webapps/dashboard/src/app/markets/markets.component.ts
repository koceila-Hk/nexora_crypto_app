import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CryptoService } from '../_services/crypto.service';
import { FooterComponent } from "../_commons/footer/footer.component";
import { HeaderComponent } from "../_commons/header/header.component";
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-crypto-details',
  standalone: true,
  imports: [CommonModule, FooterComponent, HeaderComponent, RouterLink],
  templateUrl: './markets.component.html',
  styleUrl: './markets.component.css'
})
export class MarketsComponent implements OnInit {
  @Output() coinSelected = new EventEmitter<CoinDetails>();

  coins: CoinDetails[] = [];

  constructor(private cryptoService: CryptoService) {}

  ngOnInit(): void {
      this.cryptoService.getAllCoinDetails().subscribe({
          next: data => this.coins = data,
          error: err => console.error('Erreur de chargement des cryptos', err)
        });
  }

  getChangeClass(change: number | undefined): string {
    if (change == null) return '';
    return change >= 0 ? 'text-success' : 'text-danger';
  }

  selectCoin(coin: CoinDetails) {
  this.coinSelected.emit(coin);
}
}

interface CoinDetails {
  cryptoName: string;
  icon: string;
  currentPrice: number;
  priceChangePercentage: number;
}
