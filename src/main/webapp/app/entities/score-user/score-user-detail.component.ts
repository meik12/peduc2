import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IScoreUser } from 'app/shared/model/score-user.model';

@Component({
    selector: 'jhi-score-user-detail',
    templateUrl: './score-user-detail.component.html'
})
export class ScoreUserDetailComponent implements OnInit {
    scoreUser: IScoreUser;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ scoreUser }) => {
            this.scoreUser = scoreUser;
        });
    }

    previousState() {
        window.history.back();
    }
}
