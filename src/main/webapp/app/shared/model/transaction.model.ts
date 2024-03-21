import { type IObjective } from '@/shared/model/objective.model';
import { type IPaymentMethod } from '@/shared/model/payment-method.model';
import { type IPaymentCategory } from '@/shared/model/payment-category.model';
import { type ILedger } from '@/shared/model/ledger.model';

import { type TransactionType } from '@/shared/model/enumerations/transaction-type.model';
export interface ITransaction {
  id?: number;
  amount?: number | null;
  description?: string | null;
  transactionDate?: Date | null;
  transactionType?: keyof typeof TransactionType | null;
  objective?: IObjective | null;
  paymentMethod?: IPaymentMethod | null;
  paymentCategory?: IPaymentCategory | null;
  ledger?: ILedger | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public amount?: number | null,
    public description?: string | null,
    public transactionDate?: Date | null,
    public transactionType?: keyof typeof TransactionType | null,
    public objective?: IObjective | null,
    public paymentMethod?: IPaymentMethod | null,
    public paymentCategory?: IPaymentCategory | null,
    public ledger?: ILedger | null,
  ) {}
}
