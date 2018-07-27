export interface IStudent {
    id?: number;
    name?: string;
    surname?: string;
    studentId?: string;
}

export class Student implements IStudent {
    constructor(public id?: number, public name?: string, public surname?: string, public studentId?: string) {}
}
