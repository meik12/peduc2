import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IScore } from 'app/shared/model/score.model';
import { ScoreService } from './score.service';
import { IPeer } from 'app/shared/model/peer.model';
import { PeerService } from 'app/entities/peer';

@Component({
    selector: 'jhi-score-update',
    templateUrl: './score-update.component.html'
})
export class ScoreUpdateComponent implements OnInit {
    score: IScore;
    isSaving: boolean;

    peers: IPeer[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private scoreService: ScoreService,
        private peerService: PeerService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ score }) => {
            this.score = score;
        });
        this.peerService.query().subscribe(
            (res: HttpResponse<IPeer[]>) => {
                this.peers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.score.id !== undefined) {
            this.subscribeToSaveResponse(this.scoreService.update(this.score));
        } else {
            this.subscribeToSaveResponse(this.scoreService.create(this.score));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IScore>>) {
        result.subscribe((res: HttpResponse<IScore>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPeerById(index: number, item: IPeer) {
        return item.id;
    }
}
