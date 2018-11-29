import { Moment } from 'moment';
import { IPeer } from 'app/shared/model//peer.model';
import { IUser } from 'app/core/user/user.model';

export interface ILecture {
    id?: number;
    category?: string;
    topic?: string;
    title?: string;
    keyWord?: string;
    duration?: number;
    status?: string;
    language?: string;
    videoCallLink?: string;
    presentationDate?: Moment;
    timeZone?: string;
    publicationDate?: Moment;
    peer?: IPeer;
    user?: IUser;
}

export class Lecture implements ILecture {
    constructor(
        public id?: number,
        public category?: string,
        public topic?: string,
        public title?: string,
        public keyWord?: string,
        public duration?: number,
        public status?: string,
        public language?: string,
        public videoCallLink?: string,
        public presentationDate?: Moment,
        public timeZone?: string,
        public publicationDate?: Moment,
        public peer?: IPeer,
        public user?: IUser
    ) {}
}
