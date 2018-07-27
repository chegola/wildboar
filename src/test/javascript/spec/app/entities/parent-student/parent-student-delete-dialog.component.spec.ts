/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WildboarTestModule } from '../../../test.module';
import { ParentStudentDeleteDialogComponent } from 'app/entities/parent-student/parent-student-delete-dialog.component';
import { ParentStudentService } from 'app/entities/parent-student/parent-student.service';

describe('Component Tests', () => {
    describe('ParentStudent Management Delete Component', () => {
        let comp: ParentStudentDeleteDialogComponent;
        let fixture: ComponentFixture<ParentStudentDeleteDialogComponent>;
        let service: ParentStudentService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WildboarTestModule],
                declarations: [ParentStudentDeleteDialogComponent]
            })
                .overrideTemplate(ParentStudentDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ParentStudentDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParentStudentService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
