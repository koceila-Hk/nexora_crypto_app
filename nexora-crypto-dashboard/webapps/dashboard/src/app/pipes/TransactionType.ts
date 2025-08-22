import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'transactionType',
})

export class TransacationTypePipe implements PipeTransform {
    transform(value: any, ...args: any[]) {
        if (value === 'BUY') {
            return 'Acheté';
        } else if (value === 'SELL') {
            return 'Vendu';
        } else {
            return '';
        }
    }
}