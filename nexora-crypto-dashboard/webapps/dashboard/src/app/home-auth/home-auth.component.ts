import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../common/header/header.component";
import { FooterComponent } from "../common/footer/footer.component";
import { UserService, WalletDetail, WalletService, TransactionService, Transactions } from '../services/crypto.service';
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
    transactions: Transactions[] = [];
  userId = 3;
    balance: any;

  constructor(
    private walletService: WalletService,
    private userService: UserService,
    private transactionService: TransactionService,
    private tokenStorage: TokenStorageService
  ) {}

  ngOnInit(): void {
    // const userId = this.tokenStorage.getUserIdFromToken();
    // console.log("userId : " + userId);

    if (this.userId != null) {
      this.walletService.getWalletsWithVariation(this.userId).subscribe(data => {
        this.wallets = data;
      });
  
      this.userService.getUserById(this.userId).subscribe(data => {
        this.balance = data.balance;
        console.log("balance : ", this.balance);
      });

      this.transactionService.getTransactionsByUserId(this.userId).subscribe(data => {
        // next: (data) => {
          this.transactions = data;
        });
    } else {
      console.error("User not found");
    }
  }

}
