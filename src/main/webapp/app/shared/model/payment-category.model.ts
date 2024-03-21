import { type ILedger } from '@/shared/model/ledger.model';

export interface IPaymentCategory {
  id?: number;
  code?: string | null;
  name?: string | null;
  isDefault?: boolean | null;
  isHidden?: boolean | null;
  ledger?: ILedger | null;
}

export class PaymentCategory implements IPaymentCategory {
  constructor(
    public id?: number,
    public code?: string | null,
    public name?: string | null,
    public isDefault?: boolean | null,
    public isHidden?: boolean | null,
    public ledger?: ILedger | null,
  ) {
    this.isDefault = this.isDefault ?? false;
    this.isHidden = this.isHidden ?? false;
  }
}
