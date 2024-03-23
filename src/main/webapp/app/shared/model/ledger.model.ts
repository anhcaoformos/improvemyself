import { type IUser } from '@/shared/model/user.model';

export interface ILedger {
  id?: number;
  name?: string | null;
  isDefault?: boolean | null;
  user?: IUser | null;
}

export class Ledger implements ILedger {
  constructor(
    public id?: number,
    public name?: string | null,
    public isDefault?: boolean | null,
    public user?: IUser | null,
  ) {
    this.isDefault = this.isDefault ?? false;
  }
}
