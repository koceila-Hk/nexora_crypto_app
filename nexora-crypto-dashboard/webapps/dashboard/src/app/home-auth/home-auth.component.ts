import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../common/header/header.component";
import { FooterComponent } from "../common/footer/footer.component";
import { WalletDetail, WalletService } from '../services/crypto.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TokenStorageService } from '../services/tokenStorageService';

@Component({
  selector: 'app-home-auth',
  imports: [HeaderComponent, FooterComponent, CommonModule, RouterLink],
  templateUrl: './home-auth.component.html',
  styleUrl: './home-auth.component.css'
})
export class HomeAuthComponent implements OnInit {
    wallets: WalletDetail[] = [];
  userId = 3;

  constructor(
    private walletService: WalletService,
    private tokenStorage: TokenStorageService
  ) {}

  ngOnInit(): void {
    // const userId = this.tokenStorage.getUserIdFromToken();

    this.walletService.getWalletsWithVariation(this.userId).subscribe(data => {
      this.wallets = data;
    });
  }

}
