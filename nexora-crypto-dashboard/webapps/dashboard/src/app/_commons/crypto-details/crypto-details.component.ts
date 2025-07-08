import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CryptoService } from '../../_services/crypto.service';
import { InfosCoin } from '../../_models/account';

@Component({
  selector: 'app-crypto-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './crypto-details.component.html',
  styleUrl: './crypto-details.component.css'
})
export class CryptoDetailsComponent implements OnInit {
  @Output() coinSelected = new EventEmitter<InfosCoin>();

  coins: InfosCoin[] = [];

  constructor(private cryptoService: CryptoService) { }

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

  selectCoin(coin: InfosCoin) {
    this.coinSelected.emit(coin);
  }
}

