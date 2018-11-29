import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPeer } from 'app/shared/model/peer.model';

@Component({
    selector: 'jhi-peer-detail',
    templateUrl: './peer-detail.component.html'
})
export class PeerDetailComponent implements OnInit {
    peer: IPeer;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ peer }) => {
            this.peer = peer;
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
