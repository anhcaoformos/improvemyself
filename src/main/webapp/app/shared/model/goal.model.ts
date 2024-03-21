import { type ILedger } from '@/shared/model/ledger.model';

import { type Priority } from '@/shared/model/enumerations/priority.model';
export interface IGoal {
  id?: number;
  title?: string | null;
  description?: string | null;
  priority?: keyof typeof Priority | null;
  ledger?: ILedger | null;
}

export class Goal implements IGoal {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public priority?: keyof typeof Priority | null,
    public ledger?: ILedger | null,
  ) {}
}
