export interface IProfile {
    id?: number;
}

export class Profile implements IProfile {
    constructor(public id?: number) {}
}
