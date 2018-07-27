/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WildboarTestModule } from '../../../test.module';
import { ParentStudentUpdateComponent } from 'app/entities/parent-student/parent-student-update.component';
import { ParentStudentService } from 'app/entities/parent-student/parent-student.service';
import { ParentStudent } from 'app/shared/model/parent-student.model';

describe('Component Tests', () => {
    describe('ParentStudent Management Update Component', () => {
        let comp: ParentStudentUpdateComponent;
        let fixture: ComponentFixture<ParentStudentUpdateComponent>;
        let service: ParentStudentService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WildboarTestModule],
                declarations: [ParentStudentUpdateComponent]
            })
                .overrideTemplate(ParentStudentUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ParentStudentUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParentStudentService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ParentStudent(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.parentStudent = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ParentStudent();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.parentStudent = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
