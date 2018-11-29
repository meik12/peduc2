import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILecture } from 'app/shared/model/lecture.model';

@Component({
    selector: 'jhi-lecture-detail',
    templateUrl: './lecture-detail.component.html'
})
export class LectureDetailComponent implements OnInit {
    lecture: ILecture;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ lecture }) => {
            this.lecture = lecture;
        });
    }

    previousState() {
        window.history.back();
    }
}
