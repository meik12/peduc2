import { Moment } from 'moment';
import { ILecture } from 'app/shared/model//lecture.model';
import { IUser } from 'app/core/user/user.model';

export const enum LectureStatus {
    ACTIVE = 'ACTIVE',
    SCORE = 'SCORE',
    REJECTED = 'REJECTED'
}

export interface ILectureActivity {
    id?: number;
    presentingUserId?: number;
    atendingUserId?: number;
    lectureStatus?: LectureStatus;
    presentationDate?: Moment;
    postedDate?: Moment;
    lecture?: ILecture;
    users?: IUser[];
}

export class LectureActivity implements ILectureActivity {
    constructor(
        public id?: number,
        public presentingUserId?: number,
        public atendingUserId?: number,
        public lectureStatus?: LectureStatus,
        public presentationDate?: Moment,
        public postedDate?: Moment,
        public lecture?: ILecture,
        public users?: IUser[]
    ) {}
}
