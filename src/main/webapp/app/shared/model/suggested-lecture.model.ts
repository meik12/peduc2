import { Moment } from 'moment';
import { IPeer } from 'app/shared/model//peer.model';
import { IUser } from 'app/core/user/user.model';

export interface ISuggestedLecture {
    id?: number;
    profilePictureContentType?: string;
    profilePicture?: any;
    firstName?: string;
    lastName?: string;
    averageScore?: number;
    category?: string;
    title?: string;
    duration?: number;
    language?: string;
    videoCallLink?: string;
    presentationDate?: Moment;
    timeZone?: string;
    publicationDate?: Moment;
    peer?: IPeer;
    user?: IUser;
}

export class SuggestedLecture implements ISuggestedLecture {
    constructor(
        public id?: number,
        public profilePictureContentType?: string,
        public profilePicture?: any,
        public firstName?: string,
        public lastName?: string,
        public averageScore?: number,
        public category?: string,
        public title?: string,
        public duration?: number,
        public language?: string,
        public videoCallLink?: string,
        public presentationDate?: Moment,
        public timeZone?: string,
        public publicationDate?: Moment,
        public peer?: IPeer,
        public user?: IUser
    ) {}
}
