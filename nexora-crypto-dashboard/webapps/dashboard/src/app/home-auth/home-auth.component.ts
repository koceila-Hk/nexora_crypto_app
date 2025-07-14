import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../_commons/header/header.component";
import { FooterComponent } from "../_commons/footer/footer.component";
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TokenStorageService } from '../_services/tokenStorageService';
import { UserService } from '../_services/user.service';
import {  TransactionService } from '../_services/transaction.service';
import { WalletService } from '../_services/wallet.service';
import { AccountInfosTransaction, AccountInfosWallet } from '../_models/account';

@Component({
  selector: 'app-home-auth',
  imports: [HeaderComponent, FooterComponent, CommonModule, RouterLink],
  templateUrl: './home-auth.component.html',
  styleUrl: './home-auth.component.css'
})
export class HomeAuthComponent implements OnInit {
    wallets: AccountInfosWallet[] = [];
    transactions: AccountInfosTransaction[] = [];
    balance: any;

  constructor(
    private walletService: WalletService,
    private userService: UserService,
    private transactionService: TransactionService,
    private tokenStorage: TokenStorageService
  ) {}

  ngOnInit(): void {
    const userId = this.tokenStorage.getUserIdFromToken();
    // console.log("userId : " + userId);

    if (userId != null) {
      this.walletService.getWalletsWithVariation(userId).subscribe(data => {
        this.wallets = data;
      });

      this.userService.getUserById(userId).subscribe(data => {
        this.balance = data.balance;
        // console.log("balance : ", this.balance);
      });

      this.transactionService.getTransactionsByUserId(userId).subscribe(data => {
          this.transactions = data;
        });
    } else {
      console.error("User not found");
    }
  }

}
