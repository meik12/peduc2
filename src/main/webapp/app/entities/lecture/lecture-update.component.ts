import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ILecture } from 'app/shared/model/lecture.model';
import { LectureService } from './lecture.service';
import { IPeer } from 'app/shared/model/peer.model';
import { PeerService } from 'app/entities/peer';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-lecture-update',
    templateUrl: './lecture-update.component.html'
})
export class LectureUpdateComponent implements OnInit {
    lecture: ILecture;
    isSaving: boolean;

    peers: IPeer[];

    users: IUser[];
    presentationDate: string;
    publicationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private lectureService: LectureService,
        private peerService: PeerService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ lecture }) => {
            this.lecture = lecture;
            this.presentationDate = this.lecture.presentationDate != null ? this.lecture.presentationDate.format(DATE_TIME_FORMAT) : null;
            this.publicationDate = this.lecture.publicationDate != null ? this.lecture.publicationDate.format(DATE_TIME_FORMAT) : null;
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

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.lecture.presentationDate = this.presentationDate != null ? moment(this.presentationDate, DATE_TIME_FORMAT) : null;
        this.lecture.publicationDate = this.publicationDate != null ? moment(this.publicationDate, DATE_TIME_FORMAT) : null;
        if (this.lecture.id !== undefined) {
            this.subscribeToSaveResponse(this.lectureService.update(this.lecture));
        } else {
            this.subscribeToSaveResponse(this.lectureService.create(this.lecture));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILecture>>) {
        result.subscribe((res: HttpResponse<ILecture>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
