import { type IApplicationUser } from '@/shared/model/application-user.model';

export interface ISplitBook {
  id?: number;
  description?: string | null;
  name?: string | null;
  applicationUser?: IApplicationUser | null;
}

export class SplitBook implements ISplitBook {
  constructor(
    public id?: number,
    public description?: string | null,
    public name?: string | null,
    public applicationUser?: IApplicationUser | null,
  ) {}
}
