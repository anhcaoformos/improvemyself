import { ILedger } from '@/shared/model/user-ledger.model';
import { ITransaction } from '@/shared/model/user-transaction.model';
import { IAssetBalance } from '@/shared/model/user-asset-balance.model';
import { IPaymentMethod } from '@/shared/model/user-payment-method.model';
import { IPage } from '@/shared/model/pagination.model';
import { IPaymentCategory } from '@/shared/model/user-payment-category.model';
import { IObjectivePayBook } from '@/shared/model/user-objective-pay-book.model';
import { ISummaryPaymentCategory } from '@/shared/model/summary-payment-category.model';

export interface IHome {
  ledger?: Pick<ILedger, 'id'>;
  ledgers?: ILedger[] | [];
  transactions?: IPage<ITransaction> | null;
  filterTransactionDates?: string[] | null;
  assetBalances?: IPage<IAssetBalance> | null;
  objectivePayBooks?: IPage<IObjectivePayBook> | null;
  summaryPaymentCategories?: ISummaryPaymentCategory[] | null;
  paymentMethods?: IPage<IPaymentMethod> | null;
  existingPaymentCategories?: IPaymentCategory[] | [];
  existingPaymentMethods?: IPaymentMethod[] | [];
}
