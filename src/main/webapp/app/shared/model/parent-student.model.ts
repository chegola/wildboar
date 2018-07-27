export interface IParentStudent {
    id?: number;
    userLogin?: string;
    userId?: number;
    studentName?: string;
    studentId?: number;
}

export class ParentStudent implements IParentStudent {
    constructor(
        public id?: number,
        public userLogin?: string,
        public userId?: number,
        public studentName?: string,
        public studentId?: number
    ) {}
}
