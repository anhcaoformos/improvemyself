import { type ISplitBookJoiner } from '@/shared/model/split-book-joiner.model';
import { type ISplitBook } from '@/shared/model/split-book.model';

import { type TransactionType } from '@/shared/model/enumerations/transaction-type.model';
import { type JoinerRole } from '@/shared/model/enumerations/joiner-role.model';
export interface ISplitBookDetail {
  id?: number;
  amount?: number | null;
  description?: string | null;
  personName?: string | null;
  transactionDate?: Date | null;
  transactionType?: keyof typeof TransactionType | null;
  groupId?: string | null;
  joinerRole?: keyof typeof JoinerRole | null;
  splitBookJoiner?: ISplitBookJoiner | null;
  splitBook?: ISplitBook | null;
}

export class SplitBookDetail implements ISplitBookDetail {
  constructor(
    public id?: number,
    public amount?: number | null,
    public description?: string | null,
    public personName?: string | null,
    public transactionDate?: Date | null,
    public transactionType?: keyof typeof TransactionType | null,
    public groupId?: string | null,
    public joinerRole?: keyof typeof JoinerRole | null,
    public splitBookJoiner?: ISplitBookJoiner | null,
    public splitBook?: ISplitBook | null,
  ) {}
}
