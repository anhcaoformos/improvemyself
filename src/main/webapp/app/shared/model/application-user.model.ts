export interface IApplicationUser {
  id?: number;
}

export class ApplicationUser implements IApplicationUser {
  constructor(public id?: number) {}
}
