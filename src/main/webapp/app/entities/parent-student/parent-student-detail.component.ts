import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParentStudent } from 'app/shared/model/parent-student.model';

@Component({
    selector: 'jhi-parent-student-detail',
    templateUrl: './parent-student-detail.component.html'
})
export class ParentStudentDetailComponent implements OnInit {
    parentStudent: IParentStudent;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ parentStudent }) => {
            this.parentStudent = parentStudent;
        });
    }

    previousState() {
        window.history.back();
    }
}
