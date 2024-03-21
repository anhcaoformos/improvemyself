import { type IApplicationUser } from '@/shared/model/application-user.model';

export interface ILedger {
  id?: number;
  name?: string | null;
  isDefault?: boolean | null;
  applicationUser?: IApplicationUser | null;
}

export class Ledger implements ILedger {
  constructor(
    public id?: number,
    public name?: string | null,
    public isDefault?: boolean | null,
    public applicationUser?: IApplicationUser | null,
  ) {
    this.isDefault = this.isDefault ?? false;
  }
}
