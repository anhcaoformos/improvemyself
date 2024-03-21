import { type ISplitBook } from '@/shared/model/split-book.model';

export interface ISplitBookJoiner {
  id?: number;
  name?: string | null;
  splitBook?: ISplitBook | null;
}

export class SplitBookJoiner implements ISplitBookJoiner {
  constructor(
    public id?: number,
    public name?: string | null,
    public splitBook?: ISplitBook | null,
  ) {}
}
