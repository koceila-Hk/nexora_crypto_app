import { Component, OnInit } from '@angular/core';
import { ActivatedRoute} from '@angular/router';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-crypto-detail',
  templateUrl: './chart-detail.component.html',
  styleUrls: ['./chart-detail.component.css']
})
export class ChartDetailComponent implements OnInit {
  cryptoId: string = '';
  chart: any;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.cryptoId = this.route.snapshot.paramMap.get('id')!;
    this.createChart();
  }

  createChart(): void {
    this.chart = new Chart("cryptoChart", {
      type: 'line',
      data: {
        labels: Array.from({ length: 30 }, (_, i) => i + 1),
        datasets: [{
          label: `Cours pour ${this.cryptoId.toUpperCase()}`,
          data: Array.from({ length: 30 }, () => Math.random() * 1000),
          borderColor: 'rgba(0, 168, 255, 1)',
          backgroundColor: 'rgba(0, 168, 255, 0.2)',
          tension: 0.4,
          fill: true
        }]
      },
      options: {
        responsive: true,
        scales: {
          x: {
            ticks: { color: 'white' },
            grid: { color: '#444' }
          },
          y: {
            ticks: { color: 'white' },
            grid: { color: '#444' }
          }
        },
        plugins: {
          legend: {
            labels: { color: 'white' }
          }
        }
      }
    });
  }
}
