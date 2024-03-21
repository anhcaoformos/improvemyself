import { type IPaymentCategory } from '@/shared/model/payment-category.model';
import { type ILedger } from '@/shared/model/ledger.model';

import { type ObjectiveType } from '@/shared/model/enumerations/objective-type.model';
export interface IObjective {
  id?: number;
  name?: string | null;
  type?: keyof typeof ObjectiveType | null;
  isHidden?: boolean | null;
  paymentCategory?: IPaymentCategory | null;
  ledger?: ILedger | null;
}

export class Objective implements IObjective {
  constructor(
    public id?: number,
    public name?: string | null,
    public type?: keyof typeof ObjectiveType | null,
    public isHidden?: boolean | null,
    public paymentCategory?: IPaymentCategory | null,
    public ledger?: ILedger | null,
  ) {
    this.isHidden = this.isHidden ?? false;
  }
}
