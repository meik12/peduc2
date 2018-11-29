import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Peduc2SharedModule } from 'app/shared';
import { Peduc2AdminModule } from 'app/admin/admin.module';
import {
    PeerComponent,
    PeerDetailComponent,
    PeerUpdateComponent,
    PeerDeletePopupComponent,
    PeerDeleteDialogComponent,
    peerRoute,
    peerPopupRoute
} from './';

const ENTITY_STATES = [...peerRoute, ...peerPopupRoute];

@NgModule({
    imports: [Peduc2SharedModule, Peduc2AdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PeerComponent, PeerDetailComponent, PeerUpdateComponent, PeerDeleteDialogComponent, PeerDeletePopupComponent],
    entryComponents: [PeerComponent, PeerUpdateComponent, PeerDeleteDialogComponent, PeerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Peduc2PeerModule {}
