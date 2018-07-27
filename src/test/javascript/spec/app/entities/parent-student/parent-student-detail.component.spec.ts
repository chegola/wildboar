/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WildboarTestModule } from '../../../test.module';
import { ParentStudentDetailComponent } from 'app/entities/parent-student/parent-student-detail.component';
import { ParentStudent } from 'app/shared/model/parent-student.model';

describe('Component Tests', () => {
    describe('ParentStudent Management Detail Component', () => {
        let comp: ParentStudentDetailComponent;
        let fixture: ComponentFixture<ParentStudentDetailComponent>;
        const route = ({ data: of({ parentStudent: new ParentStudent(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WildboarTestModule],
                declarations: [ParentStudentDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ParentStudentDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ParentStudentDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.parentStudent).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
