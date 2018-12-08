import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IScoreUser } from 'app/shared/model/score-user.model';
import { ScoreUserService } from './score-user.service';
import { IUser, UserService } from 'app/core';
import { ILecture } from 'app/shared/model/lecture.model';
import { LectureService } from 'app/entities/lecture';
import { ILectureActivity } from 'app/shared/model/lecture-activity.model';
import { LectureActivityService } from 'app/entities/lecture-activity';
import { createOfflineCompileUrlResolver } from '@angular/compiler';

@Component({
    selector: 'jhi-score-user-update',
    templateUrl: './score-user-update.component.html'
})
export class ScoreUserUpdateComponent implements OnInit {
    scoreUser: IScoreUser;
    isSaving: boolean;

    users: IUser[];

    lectures: ILecture[];

    lectureactivities: ILectureActivity[];
    selectedScore = [''];

    scores = [
        { name: 'Excellent', value: '0' },
        { name: 'Very Good', value: '0' },
        { name: 'Fair', value: '0' },
        { name: 'Bad', value: '0' }
    ];
    constructor(
        private jhiAlertService: JhiAlertService,
        private scoreUserService: ScoreUserService,
        private userService: UserService,
        private lectureService: LectureService,
        private lectureActivityService: LectureActivityService,
        private activatedRoute: ActivatedRoute
    ) {}

    getValue(event: any, p) {
        if (event.target.checked) {
            this.selectedScore.pop();
            this.selectedScore.push(p.name);
            this.scoreUser.excellent = this.selectedScore[0] === 'Excellent' ? 1 : 0;
            this.scoreUser.veryGood = this.selectedScore[0] === 'Very Good' ? 1 : 0;
            this.scoreUser.bad = this.selectedScore[0] === 'Bad' ? 1 : 0;
            this.scoreUser.fair = this.selectedScore[0] === 'Fair' ? 1 : 0;

            console.log(this.selectedScore);
        }
    }
    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ scoreUser }) => {
            this.scoreUser = scoreUser;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.lectureService.query().subscribe(
            (res: HttpResponse<ILecture[]>) => {
                this.lectures = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.lectureActivityService.query().subscribe(
            (res: HttpResponse<ILectureActivity[]>) => {
                this.lectureactivities = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.scoreUser.id !== undefined) {
            this.scoreUser.description = 'DONE';
            this.subscribeToSaveResponse(this.scoreUserService.update(this.scoreUser));
        } else {
            this.subscribeToSaveResponse(this.scoreUserService.create(this.scoreUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IScoreUser>>) {
        result.subscribe((res: HttpResponse<IScoreUser>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackLectureById(index: number, item: ILecture) {
        return item.id;
    }

    trackLectureActivityById(index: number, item: ILectureActivity) {
        return item.id;
    }
}
