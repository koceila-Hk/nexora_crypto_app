export class AccountInfosUser {
    id!: number;
    // username: string;
    balance: number | undefined;
}

export class AccountInfosWallet{
    cryptoName!: string;
    quantity: number | undefined;
    icon!: string;
    variationPercentage!: number;
}

export class AccountInfosTransaction{
    cryptoName!: string;
    quantity!: number;
    unitPrice!: number;
    type!: string;
    totalAmount!: number;
    dateTransaction!: string;
}

export class InfosCoin{
    cryptoName!: string;
    icon!: string;
    currentPrice!: number;
    priceChangePercentage!: number;
}

interface CoinDetails {
  cryptoName: string;
  icon: string;
  currentPrice: number;
  priceChangePercentage: number;
}