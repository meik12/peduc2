import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ILectureActivity } from 'app/shared/model/lecture-activity.model';
import { Principal } from 'app/core';
import { LectureActivityService } from './lecture-activity.service';

@Component({
    selector: 'jhi-lecture-activity',
    templateUrl: './lecture-activity.component.html'
})
export class LectureActivityComponent implements OnInit, OnDestroy {
    lectureActivities: ILectureActivity[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private lectureActivityService: LectureActivityService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.lectureActivityService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<ILectureActivity[]>) => (this.lectureActivities = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.lectureActivityService.query().subscribe(
            (res: HttpResponse<ILectureActivity[]>) => {
                this.lectureActivities = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInLectureActivities();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ILectureActivity) {
        return item.id;
    }

    registerChangeInLectureActivities() {
        this.eventSubscriber = this.eventManager.subscribe('lectureActivityListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
