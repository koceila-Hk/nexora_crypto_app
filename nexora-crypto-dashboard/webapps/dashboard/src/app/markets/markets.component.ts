import { Component } from '@angular/core';
import { HeaderComponent } from "../common/header/header.component";
import { FooterComponent } from "../common/footer/footer.component";
import { CommonModule } from '@angular/common';
import { CryptoDetailsComponent } from '../common/crypto-details/crypto-details.component';

@Component({
  selector: 'app-markets',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, CommonModule, CryptoDetailsComponent],
  templateUrl: './markets.component.html',
  styleUrl: './markets.component.css'
})
export class MarketsComponent {
}

