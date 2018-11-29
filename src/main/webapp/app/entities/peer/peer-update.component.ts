import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IPeer } from 'app/shared/model/peer.model';
import { PeerService } from './peer.service';
import { IScore } from 'app/shared/model/score.model';
import { ScoreService } from 'app/entities/score';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-peer-update',
    templateUrl: './peer-update.component.html'
})
export class PeerUpdateComponent implements OnInit {
    peer: IPeer;
    isSaving: boolean;

    scores: IScore[];

    users: IUser[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private peerService: PeerService,
        private scoreService: ScoreService,
        private userService: UserService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ peer }) => {
            this.peer = peer;
        });
        this.scoreService.query({ filter: 'peer-is-null' }).subscribe(
            (res: HttpResponse<IScore[]>) => {
                if (!this.peer.score || !this.peer.score.id) {
                    this.scores = res.body;
                } else {
                    this.scoreService.find(this.peer.score.id).subscribe(
                        (subRes: HttpResponse<IScore>) => {
                            this.scores = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.peer, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.peer.id !== undefined) {
            this.subscribeToSaveResponse(this.peerService.update(this.peer));
        } else {
            this.subscribeToSaveResponse(this.peerService.create(this.peer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPeer>>) {
        result.subscribe((res: HttpResponse<IPeer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackScoreById(index: number, item: IScore) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
