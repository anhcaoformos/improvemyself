import { type ILedger } from '@/shared/model/ledger.model';

import { type PaymentMethodType } from '@/shared/model/enumerations/payment-method-type.model';
export interface IPaymentMethod {
  id?: number;
  code?: string | null;
  name?: string | null;
  type?: keyof typeof PaymentMethodType | null;
  isHidden?: boolean | null;
  ledger?: ILedger | null;
}

export class PaymentMethod implements IPaymentMethod {
  constructor(
    public id?: number,
    public code?: string | null,
    public name?: string | null,
    public type?: keyof typeof PaymentMethodType | null,
    public isHidden?: boolean | null,
    public ledger?: ILedger | null,
  ) {
    this.isHidden = this.isHidden ?? false;
  }
}
