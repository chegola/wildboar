import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ParentStudent } from 'app/shared/model/parent-student.model';
import { ParentStudentService } from './parent-student.service';
import { ParentStudentComponent } from './parent-student.component';
import { ParentStudentDetailComponent } from './parent-student-detail.component';
import { ParentStudentUpdateComponent } from './parent-student-update.component';
import { ParentStudentDeletePopupComponent } from './parent-student-delete-dialog.component';
import { IParentStudent } from 'app/shared/model/parent-student.model';

@Injectable({ providedIn: 'root' })
export class ParentStudentResolve implements Resolve<IParentStudent> {
    constructor(private service: ParentStudentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((parentStudent: HttpResponse<ParentStudent>) => parentStudent.body));
        }
        return of(new ParentStudent());
    }
}

export const parentStudentRoute: Routes = [
    {
        path: 'parent-student',
        component: ParentStudentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'wildboarApp.parentStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'parent-student/:id/view',
        component: ParentStudentDetailComponent,
        resolve: {
            parentStudent: ParentStudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wildboarApp.parentStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'parent-student/new',
        component: ParentStudentUpdateComponent,
        resolve: {
            parentStudent: ParentStudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wildboarApp.parentStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'parent-student/:id/edit',
        component: ParentStudentUpdateComponent,
        resolve: {
            parentStudent: ParentStudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wildboarApp.parentStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const parentStudentPopupRoute: Routes = [
    {
        path: 'parent-student/:id/delete',
        component: ParentStudentDeletePopupComponent,
        resolve: {
            parentStudent: ParentStudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wildboarApp.parentStudent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
