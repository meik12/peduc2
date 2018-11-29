import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ISuggestedLecture } from 'app/shared/model/suggested-lecture.model';
import { SuggestedLectureService } from './suggested-lecture.service';
import { IPeer } from 'app/shared/model/peer.model';
import { PeerService } from 'app/entities/peer';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-suggested-lecture-update',
    templateUrl: './suggested-lecture-update.component.html'
})
export class SuggestedLectureUpdateComponent implements OnInit {
    suggestedLecture: ISuggestedLecture;
    isSaving: boolean;

    peers: IPeer[];

    users: IUser[];
    presentationDate: string;
    publicationDate: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private suggestedLectureService: SuggestedLectureService,
        private peerService: PeerService,
        private userService: UserService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ suggestedLecture }) => {
            this.suggestedLecture = suggestedLecture;
            this.presentationDate =
                this.suggestedLecture.presentationDate != null ? this.suggestedLecture.presentationDate.format(DATE_TIME_FORMAT) : null;
            this.publicationDate =
                this.suggestedLecture.publicationDate != null ? this.suggestedLecture.publicationDate.format(DATE_TIME_FORMAT) : null;
        });
        this.peerService.query().subscribe(
            (res: HttpResponse<IPeer[]>) => {
                this.peers = res.body;
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
        this.dataUtils.clearInputImage(this.suggestedLecture, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.suggestedLecture.presentationDate = this.presentationDate != null ? moment(this.presentationDate, DATE_TIME_FORMAT) : null;
        this.suggestedLecture.publicationDate = this.publicationDate != null ? moment(this.publicationDate, DATE_TIME_FORMAT) : null;
        if (this.suggestedLecture.id !== undefined) {
            this.subscribeToSaveResponse(this.suggestedLectureService.update(this.suggestedLecture));
        } else {
            this.subscribeToSaveResponse(this.suggestedLectureService.create(this.suggestedLecture));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISuggestedLecture>>) {
        result.subscribe((res: HttpResponse<ISuggestedLecture>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
