import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ILectureActivity } from 'app/shared/model/lecture-activity.model';
import { LectureActivityService } from './lecture-activity.service';
import { ILecture } from 'app/shared/model/lecture.model';
import { LectureService } from 'app/entities/lecture';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-lecture-activity-update',
    templateUrl: './lecture-activity-update.component.html'
})
export class LectureActivityUpdateComponent implements OnInit {
    lectureActivity: ILectureActivity;
    isSaving: boolean;

    lectures: ILecture[];

    users: IUser[];
    presentationDate: string;
    postedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private lectureActivityService: LectureActivityService,
        private lectureService: LectureService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ lectureActivity }) => {
            this.lectureActivity = lectureActivity;
            this.presentationDate =
                this.lectureActivity.presentationDate != null ? this.lectureActivity.presentationDate.format(DATE_TIME_FORMAT) : null;
            this.postedDate = this.lectureActivity.postedDate != null ? this.lectureActivity.postedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.lectureService.query({ filter: 'lectureactivity-is-null' }).subscribe(
            (res: HttpResponse<ILecture[]>) => {
                if (!this.lectureActivity.lecture || !this.lectureActivity.lecture.id) {
                    this.lectures = res.body;
                } else {
                    this.lectureService.find(this.lectureActivity.lecture.id).subscribe(
                        (subRes: HttpResponse<ILecture>) => {
                            this.lectures = [subRes.body].concat(res.body);
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

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.lectureActivity.presentationDate = this.presentationDate != null ? moment(this.presentationDate, DATE_TIME_FORMAT) : null;
        this.lectureActivity.postedDate = this.postedDate != null ? moment(this.postedDate, DATE_TIME_FORMAT) : null;
        if (this.lectureActivity.id !== undefined) {
            this.subscribeToSaveResponse(this.lectureActivityService.update(this.lectureActivity));
        } else {
            this.subscribeToSaveResponse(this.lectureActivityService.create(this.lectureActivity));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILectureActivity>>) {
        result.subscribe((res: HttpResponse<ILectureActivity>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackLectureById(index: number, item: ILecture) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
