import { Component, effect, OnInit } from '@angular/core';
import { HeaderComponent } from "../_commons/header/header.component";
import { FooterComponent } from "../_commons/footer/footer.component";
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TokenStorageService } from '../_services/tokenStorageService';
import { UserService } from '../_services/user.service';
import { TransactionService } from '../_services/transaction.service';
import { WalletService } from '../_services/wallet.service';
import { AccountInfosTransaction, AccountInfosWallet } from '../_models/account';
import { TransacationTypePipe } from '../pipes/TransactionType';
import { AuthService } from '../_services/AuthService';

@Component({
  selector: 'app-home-auth',
  imports: [HeaderComponent, FooterComponent, CommonModule, RouterLink, TransacationTypePipe],
  templateUrl: './home-auth.component.html',
  styleUrl: './home-auth.component.css'
})
export class HomeAuthComponent  {
  wallets: AccountInfosWallet[] = [];
  transactions: AccountInfosTransaction[] = [];
  balance: any;

  constructor(
    private walletService: WalletService,
    private userService: UserService,
    private transactionService: TransactionService,
    private tokenStorage: TokenStorageService,
    private authService: AuthService
  ) { 
    effect(() => {
        const user = this.authService.currentUser();
        // console.log('user :', user);
        if (user != null) {
          const userId = user.id;
          // console.log('userId :', userId);
          this.balance = user.balance;

          this.walletService.getWalletsWithVariation(userId).subscribe(data => {
            this.wallets = data;
          });
    
          // this.userService.getUserById(userId).subscribe(data => {
          //   this.balance = data.balance;
          //   // console.log("balance : ", this.balance);
          // });
    
          this.transactionService.getTransactionsByUserId(userId).subscribe(data => {
            this.transactions = data;
          });
        }
        // } else {
        //   console.error("User not found");
        // }
    })
  }

}
