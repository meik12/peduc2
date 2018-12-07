import { IUser } from 'app/core/user/user.model';
import { ILecture } from 'app/shared/model//lecture.model';
import { ILectureActivity } from 'app/shared/model//lecture-activity.model';

export interface IScoreUser {
    id?: number;
    excellent?: number;
    veryGood?: number;
    fair?: number;
    bad?: number;
    average?: number;
    description?: string;
    user?: IUser;
    lecture?: ILecture;
    lectureActivity?: ILectureActivity;
}

export class ScoreUser implements IScoreUser {
    constructor(
        public id?: number,
        public excellent?: number,
        public veryGood?: number,
        public fair?: number,
        public bad?: number,
        public average?: number,
        public description?: string,
        public user?: IUser,
        public lecture?: ILecture,
        public lectureActivity?: ILectureActivity
    ) {}
}
