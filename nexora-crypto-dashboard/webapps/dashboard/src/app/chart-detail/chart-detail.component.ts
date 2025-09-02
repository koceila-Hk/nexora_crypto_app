import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
<<<<<<< HEAD
import { ActivatedRoute } from '@angular/router';
=======
import { ActivatedRoute, Router } from '@angular/router';
>>>>>>> develop
import { createChart, IChartApi, LineStyle, Time } from 'lightweight-charts';
import { InfosCoin } from '../_models/account';
import { HeaderComponent } from '../_commons/header/header.component';
import { FooterComponent } from '../_commons/footer/footer.component';
import { FormsModule } from '@angular/forms';
<<<<<<< HEAD
import { CryptoService } from '../_services/crypto.service';
=======
import { CoinService } from '../_services/coin.service';
import { AuthService } from '../_services/AuthService';
import { environment } from '../../environments/envionment';
import { HttpClient } from '@angular/common/http';
>>>>>>> develop

@Component({
  selector: 'app-crypto-detail',
  templateUrl: './chart-detail.component.html',
  styleUrls: ['./chart-detail.component.css'],
  standalone: true,
  imports: [HeaderComponent, FooterComponent, FormsModule]
})
export class ChartDetailComponent implements OnInit, AfterViewInit {
  @ViewChild('chartContainer', { static: true }) chartContainer!: ElementRef;
  chart!: IChartApi;
  cryptoId: string = '';
  selectedCoin: InfosCoin | null = null;
<<<<<<< HEAD

  mode: 'buy' | 'sell' = 'buy';
  amountInput: number = 0;
  resultAmount: number = 0;

  constructor(private route: ActivatedRoute, private cryptoService: CryptoService) { }
=======
  mode: 'buy' | 'sell' = 'buy';
  amountInput: number = 0;
  resultAmount: number = 0;
  errorMessage: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private coinService: CoinService,
    private authService: AuthService) { }
>>>>>>> develop
  ngAfterViewInit(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
    this.cryptoId = this.route.snapshot.paramMap.get('id') || '';

<<<<<<< HEAD
    this.cryptoService.getAllCoinDetails().subscribe({
=======
    this.coinService.getAllCoinDetails().subscribe({
>>>>>>> develop
      next: (data) => {
        const coin = data.find(c => c.cryptoName.toLowerCase() === this.cryptoId.toLowerCase());
        if (coin) {
          this.selectedCoin = coin;
          this.initChartWithPrice(coin.currentPrice);
        } else {
          console.error('Crypto introuvable');
        }
      },
      error: (err) => console.error('Erreur lors du chargement de la crypto', err)
    });
  }

  initChartWithPrice(basePrice: number): void {
    this.chart = createChart(this.chartContainer.nativeElement, {
      width: this.chartContainer.nativeElement.offsetWidth,
      height: 330,
      layout: {
        background: { color: '#0e0e0e' },
        textColor: '#c9d1d9'
      },
      grid: {
        vertLines: { color: 'hsla(250, 47%, 53%, 1.00)' },
        horzLines: { color: 'rgba(250, 47%, 53%, 1.00)' }
      },
      timeScale: {
        borderColor: 'rgb(49, 25, 172)',
        timeVisible: true
      },
      rightPriceScale: {
        borderColor: 'rgb(49, 25, 172)',
        autoScale: true
      },
      crosshair: {
        mode: 1
      }
    });

    const now = Math.floor(Date.now() / 1000);
    const data = Array.from({ length: 48 }, (_, i) => ({
      time: (now - (48 - i) * 1800) as Time,
      value: Number((basePrice + (Math.random() - 0.5) * 1000).toFixed(2))
    }));

    const areaSeries = this.chart.addAreaSeries({
      topColor: 'rgba(252, 213, 53, 0.4)',
      bottomColor: 'rgba(252, 213, 53, 0)',
      lineColor: 'rgba(252, 213, 53, 1)',
      lineWidth: 2,
      priceFormat: {
        type: 'custom',
        formatter: (price: number) => price.toFixed(2) + ' €'
      },
      crosshairMarkerVisible: false
    });

    areaSeries.setData(data);
  }

  onCoinSelected(coin: InfosCoin): void {
    this.selectedCoin = coin;
    this.calculateConversion();
  }

  calculateConversion(): void {
    if (this.selectedCoin && this.amountInput > 0) {
      const price = this.selectedCoin.currentPrice;
      this.resultAmount =
        this.mode === 'buy'
          ? this.amountInput / price
          : this.amountInput * price;
    } else {
      this.resultAmount = 0;
    }
  }

  submit(): void {
<<<<<<< HEAD
    console.log('Submitted', this.mode, this.amountInput, this.resultAmount);
=======
    console.log('click buy');
    if (!this.selectedCoin) {
      this.errorMessage = 'Veuillez sélectionner une cryptomonnaie.';
      return;
    }

    // const userId = this.tokenStorage.getUserIdFromToken();
    const userId = this.authService.currentUser()?.id;
    // console.log(userId);
    if (userId != null) {
      if (this.selectedCoin && this.amountInput > 0) {
        let transaction: any;

        if (this.mode === 'buy') {
          transaction = {
            userId,
            cryptoName: this.selectedCoin.cryptoName,
            quantity: this.resultAmount,          // quantité achetée
            unitPrice: this.selectedCoin.currentPrice,
            totalAmount: this.amountInput,        // montant dépensé
            type: 'BUY'
          };
        } else {
          transaction = {
            userId,
            cryptoName: this.selectedCoin.cryptoName,
            quantity: this.amountInput,           // quantité vendue
            unitPrice: this.selectedCoin.currentPrice,
            totalAmount: this.resultAmount,       // montant obtenu en points
            type: 'SELL'
          };
        }
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
>>>>>>> develop
  }
}
