import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParentStudent } from 'app/shared/model/parent-student.model';
import { ParentStudentService } from './parent-student.service';

@Component({
    selector: 'jhi-parent-student-delete-dialog',
    templateUrl: './parent-student-delete-dialog.component.html'
})
export class ParentStudentDeleteDialogComponent {
    parentStudent: IParentStudent;

    constructor(
        private parentStudentService: ParentStudentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.parentStudentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'parentStudentListModification',
                content: 'Deleted an parentStudent'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-parent-student-delete-popup',
    template: ''
})
export class ParentStudentDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ parentStudent }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ParentStudentDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.parentStudent = parentStudent;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
