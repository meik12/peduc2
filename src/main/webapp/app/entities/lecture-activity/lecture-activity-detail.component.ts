import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILectureActivity } from 'app/shared/model/lecture-activity.model';

@Component({
    selector: 'jhi-lecture-activity-detail',
    templateUrl: './lecture-activity-detail.component.html'
})
export class LectureActivityDetailComponent implements OnInit {
    lectureActivity: ILectureActivity;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ lectureActivity }) => {
            this.lectureActivity = lectureActivity;
        });
    }

    previousState() {
        window.history.back();
    }
}
