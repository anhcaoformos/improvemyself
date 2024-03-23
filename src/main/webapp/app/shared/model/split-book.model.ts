import { type IUser } from '@/shared/model/user.model';

export interface ISplitBook {
  id?: number;
  description?: string | null;
  name?: string | null;
  user?: IUser | null;
}

export class SplitBook implements ISplitBook {
  constructor(
    public id?: number,
    public description?: string | null,
    public name?: string | null,
    public user?: IUser | null,
  ) {}
}
