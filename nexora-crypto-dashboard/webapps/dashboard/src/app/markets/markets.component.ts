import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoinService } from '../_services/coin.service';
import { FooterComponent } from "../_commons/footer/footer.component";
import { HeaderComponent } from "../_commons/header/header.component";
import { Router } from '@angular/router';
import { CoinDetails } from '../_models/account';
@Component({
  selector: 'app-crypto-details',
  standalone: true,
  imports: [CommonModule, FooterComponent, HeaderComponent],
  templateUrl: './markets.component.html',
  styleUrl: './markets.component.css'
})
export class MarketsComponent implements OnInit {
  @Output() coinSelected = new EventEmitter<CoinDetails>();

  coins: CoinDetails[] = [];

  constructor(
    private coinsService: CoinService,
    private router: Router
  ) 
{ }

  ngOnInit(): void {
    this.coinsService.getAllCoinDetails().subscribe({
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

  openCoinInNewTab(coin: any): void {
    const encodedId = encodeURIComponent(coin.cryptoName.toLowerCase());
    // const url = `${window.location.origin}/#/crypto-chart/${encodedId}`;
    // window.open(url, '_blank');
    this.router.navigate([`/crypto-chart/${encodedId}`]);
  }


}