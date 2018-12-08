import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IScoreUser } from 'app/shared/model/score-user.model';
import { Principal } from 'app/core';
import { ScoreUserService } from './score-user.service';

@Component({
    selector: 'jhi-score-user',
    templateUrl: './score-user.average.component.html'
})
export class ScoreUserAverageComponent implements OnInit, OnDestroy {
    scoreUsers: IScoreUser[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private scoreUserService: ScoreUserService,
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
            this.scoreUserService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IScoreUser[]>) => (this.scoreUsers = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.scoreUserService.queryAverage().subscribe(
            (res: HttpResponse<IScoreUser[]>) => {
                this.scoreUsers = res.body;
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
        this.registerChangeInScoreUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IScoreUser) {
        return item.id;
    }

    registerChangeInScoreUsers() {
        this.eventSubscriber = this.eventManager.subscribe('scoreUserListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
