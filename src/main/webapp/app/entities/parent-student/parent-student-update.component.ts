import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IParentStudent } from 'app/shared/model/parent-student.model';
import { ParentStudentService } from './parent-student.service';
import { IUser, UserService, Principal, Account } from 'app/core';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student';

@Component({
    selector: 'jhi-parent-student-update',
    templateUrl: './parent-student-update.component.html'
})
export class ParentStudentUpdateComponent implements OnInit {
    private _parentStudent: IParentStudent;
    isSaving: boolean;

    // users: IUser[];

    user: IUser;

    students: IStudent[];

    account: Account;

    parentStudents: IParentStudent[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private parentStudentService: ParentStudentService,
        private userService: UserService,
        private studentService: StudentService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ parentStudent }) => {
            this.parentStudent = parentStudent;
        });
        this.principal.identity().then(account => {
            this.account = account;
            this.loadByCurrentUser();
        });
        // this.userService.query().subscribe(
        //     (res: HttpResponse<IUser[]>) => {
        //         this.users = res.body;
        //     },
        //     (res: HttpErrorResponse) => this.onError(res.message)
        // );
        this.studentService.query().subscribe(
            (res: HttpResponse<IStudent[]>) => {
                this.students = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadByCurrentUser() {
        this.parentStudentService
            .findByParent()
            .subscribe(
                (res: HttpResponse<IParentStudent[]>) => this.loadParentStudent(res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    loadParentStudent(data: IParentStudent[]) {
        this.parentStudents = data;
    }

    save() {
        this.isSaving = true;
        this.userService.find(this.account.login).subscribe(
            (res: HttpResponse<IUser>) => {
                this.user = res.body;
                this.parentStudent.userId = this.user.id;
                // this.parentStudent.userLogin = this.user.login;
                if (this.parentStudent.id !== undefined) {
                    this.subscribeToSaveResponse(this.parentStudentService.update(this.parentStudent));
                } else {
                    this.subscribeToSaveResponse(this.parentStudentService.create(this.parentStudent));
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IParentStudent>>) {
        result.subscribe((res: HttpResponse<IParentStudent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackStudentById(index: number, item: IStudent) {
        return item.id;
    }
    get parentStudent() {
        return this._parentStudent;
    }

    set parentStudent(parentStudent: IParentStudent) {
        this._parentStudent = parentStudent;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }
}
