import { Component } from '@angular/core';
import { HeaderComponent } from "../_commons/header/header.component";
import { FooterComponent } from "../_commons/footer/footer.component";
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CryptoDetailsComponent } from '../_commons/crypto-details/crypto-details.component';

@Component({
  selector: 'app-home-not-auth',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, RouterLink, CommonModule, CryptoDetailsComponent],
  templateUrl: './home-not-auth.component.html',
  styleUrl: './home-not-auth.component.css'
})
export class HomeNotAuthComponent {
}