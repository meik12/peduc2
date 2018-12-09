import { Component, OnInit, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
@Component({
    selector: 'jhi-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
    constructor(@Inject(DOCUMENT) private document: any) {}

    ngOnInit() {}
    goToFacebook(): void {
        this.document.location.href = 'https://www.facebook.com/';
    }
}
