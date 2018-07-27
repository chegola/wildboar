import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WildboarStudentModule } from './student/student.module';
import { WildboarParentStudentModule } from './parent-student/parent-student.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        WildboarStudentModule,
        WildboarParentStudentModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WildboarEntityModule {}
