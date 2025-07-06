import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { createChart, IChartApi, LineStyle, Time, LineSeriesPartialOptions } from 'lightweight-charts';
import { HeaderComponent } from '../_commons/header/header.component';
import { FooterComponent } from '../_commons/footer/footer.component';
import { InfosCoin } from '../_models/account';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-crypto-detail',
  templateUrl: './chart-detail.component.html',
  styleUrls: ['./chart-detail.component.css'],
  imports: [HeaderComponent, FooterComponent, FormsModule]
})
export class ChartDetailComponent implements OnInit, AfterViewInit {
  @ViewChild('chartContainer', { static: true }) chartContainer!: ElementRef;
  chart!: IChartApi;
  cryptoId: string = '';
  selectedCoin: InfosCoin | null = null;
  mode: 'buy' | 'sell' = 'buy';
  amountInput: number = 0;
  resultAmount: number = 0;
  errorMessage: string = '';

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.cryptoId = this.route.snapshot.paramMap.get('id') || 'btc';
  }

  ngAfterViewInit(): void {
    this.initChart();
  }

  initChart(): void {
    this.chart = createChart(this.chartContainer.nativeElement, {
      width: this.chartContainer.nativeElement.offsetWidth,
      height: 400,
      layout: {
        background: { color: '#1e1e1e' },
        textColor: '#d1d4dc',
      },
      grid: {
        vertLines: { color: '#2b2b2b' },
        horzLines: { color: '#2b2b2b' },
      },
      timeScale: {
        borderColor: '#485c7b',
        timeVisible: true,
      },
    });


    const seriesOptions: LineSeriesPartialOptions = {
      color: '#00aaff',
      lineStyle: LineStyle.Solid,
      lineWidth: 2,
    };

    const lineSeries = this.chart.addLineSeries(seriesOptions);

    const data = Array.from({ length: 30 }, (_, i) => ({
      time: (Date.now() / 1000 - (30 - i) * 3600) as Time,
      value: Math.floor(Math.random() * 1000),
    }));

    lineSeries.setData(data);
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

  submit() {
    console.log('Submitted', this.mode, this.amountInput, this.resultAmount);
  }
}
