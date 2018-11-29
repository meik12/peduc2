import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISuggestedLecture } from 'app/shared/model/suggested-lecture.model';
import { SuggestedLectureService } from './suggested-lecture.service';

@Component({
    selector: 'jhi-suggested-lecture-delete-dialog',
    templateUrl: './suggested-lecture-delete-dialog.component.html'
})
export class SuggestedLectureDeleteDialogComponent {
    suggestedLecture: ISuggestedLecture;

    constructor(
        private suggestedLectureService: SuggestedLectureService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.suggestedLectureService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'suggestedLectureListModification',
                content: 'Deleted an suggestedLecture'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-suggested-lecture-delete-popup',
    template: ''
})
export class SuggestedLectureDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ suggestedLecture }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SuggestedLectureDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.suggestedLecture = suggestedLecture;
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
