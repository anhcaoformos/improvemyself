import { type IApplicationUser } from '@/shared/model/application-user.model';

import { type NoteType } from '@/shared/model/enumerations/note-type.model';
import { type RepeatType } from '@/shared/model/enumerations/repeat-type.model';
import { type AlertType } from '@/shared/model/enumerations/alert-type.model';
export interface INote {
  id?: number;
  title?: string | null;
  description?: string | null;
  noteDateFrom?: Date | null;
  noteDateTo?: Date | null;
  noteType?: keyof typeof NoteType | null;
  repeatType?: keyof typeof RepeatType | null;
  alertType?: keyof typeof AlertType | null;
  applicationUser?: IApplicationUser | null;
}

export class Note implements INote {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public noteDateFrom?: Date | null,
    public noteDateTo?: Date | null,
    public noteType?: keyof typeof NoteType | null,
    public repeatType?: keyof typeof RepeatType | null,
    public alertType?: keyof typeof AlertType | null,
    public applicationUser?: IApplicationUser | null,
  ) {}
}
