import { IScore } from 'app/shared/model//score.model';
import { IUser } from 'app/core/user/user.model';

export interface IPeer {
    id?: number;
    profilePictureContentType?: string;
    profilePicture?: any;
    firstName?: string;
    lastName?: string;
    averageScore?: number;
    interestAreas?: string;
    email?: string;
    password?: string;
    score?: IScore;
    user?: IUser;
}

export class Peer implements IPeer {
    constructor(
        public id?: number,
        public profilePictureContentType?: string,
        public profilePicture?: any,
        public firstName?: string,
        public lastName?: string,
        public averageScore?: number,
        public interestAreas?: string,
        public email?: string,
        public password?: string,
        public score?: IScore,
        public user?: IUser
    ) {}
}
