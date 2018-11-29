import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISuggestedLecture } from 'app/shared/model/suggested-lecture.model';

@Component({
    selector: 'jhi-suggested-lecture-detail',
    templateUrl: './suggested-lecture-detail.component.html'
})
export class SuggestedLectureDetailComponent implements OnInit {
    suggestedLecture: ISuggestedLecture;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ suggestedLecture }) => {
            this.suggestedLecture = suggestedLecture;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
