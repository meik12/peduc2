import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IScoreUser } from 'app/shared/model/score-user.model';
import { ScoreUserService } from './score-user.service';

@Component({
    selector: 'jhi-score-user-delete-dialog',
    templateUrl: './score-user-delete-dialog.component.html'
})
export class ScoreUserDeleteDialogComponent {
    scoreUser: IScoreUser;

    constructor(private scoreUserService: ScoreUserService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.scoreUserService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'scoreUserListModification',
                content: 'Deleted an scoreUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-score-user-delete-popup',
    template: ''
})
export class ScoreUserDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ scoreUser }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ScoreUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.scoreUser = scoreUser;
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
