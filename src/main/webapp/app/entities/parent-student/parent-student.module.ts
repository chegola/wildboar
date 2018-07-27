import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WildboarSharedModule } from 'app/shared';
import { WildboarAdminModule } from 'app/admin/admin.module';
import {
    ParentStudentComponent,
    ParentStudentDetailComponent,
    ParentStudentUpdateComponent,
    ParentStudentDeletePopupComponent,
    ParentStudentDeleteDialogComponent,
    parentStudentRoute,
    parentStudentPopupRoute
} from './';

const ENTITY_STATES = [...parentStudentRoute, ...parentStudentPopupRoute];

@NgModule({
    imports: [WildboarSharedModule, WildboarAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ParentStudentComponent,
        ParentStudentDetailComponent,
        ParentStudentUpdateComponent,
        ParentStudentDeleteDialogComponent,
        ParentStudentDeletePopupComponent
    ],
    entryComponents: [
        ParentStudentComponent,
        ParentStudentUpdateComponent,
        ParentStudentDeleteDialogComponent,
        ParentStudentDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WildboarParentStudentModule {}
