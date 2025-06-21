import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CryptoService } from '../../services/crypto.service';

@Component({
  selector: 'app-crypto-details',
  standalone: true,
  imports: [ CommonModule],
  templateUrl: './crypto-details.component.html',
  styleUrl: './crypto-details.component.css'
})
export class CryptoDetailsComponent implements OnInit {
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
