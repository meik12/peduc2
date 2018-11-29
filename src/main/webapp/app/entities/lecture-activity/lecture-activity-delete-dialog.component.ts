import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILectureActivity } from 'app/shared/model/lecture-activity.model';
import { LectureActivityService } from './lecture-activity.service';

@Component({
    selector: 'jhi-lecture-activity-delete-dialog',
    templateUrl: './lecture-activity-delete-dialog.component.html'
})
export class LectureActivityDeleteDialogComponent {
    lectureActivity: ILectureActivity;

    constructor(
        private lectureActivityService: LectureActivityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.lectureActivityService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'lectureActivityListModification',
                content: 'Deleted an lectureActivity'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-lecture-activity-delete-popup',
    template: ''
})
export class LectureActivityDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ lectureActivity }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(LectureActivityDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.lectureActivity = lectureActivity;
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
