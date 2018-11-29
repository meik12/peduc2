import { IPeer } from 'app/shared/model//peer.model';

export interface IScore {
    id?: number;
    excellent?: number;
    veryGood?: number;
    fair?: number;
    bad?: number;
    peer?: IPeer;
}

export class Score implements IScore {
    constructor(
        public id?: number,
        public excellent?: number,
        public veryGood?: number,
        public fair?: number,
        public bad?: number,
        public peer?: IPeer
    ) {}
}
