import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Peduc2SharedModule } from 'app/shared';
import { Peduc2AdminModule } from 'app/admin/admin.module';
import {
    ScoreUserComponent,
    ScoreUserDetailComponent,
    ScoreUserUpdateComponent,
    ScoreUserDeletePopupComponent,
    ScoreUserDeleteDialogComponent,
    scoreUserRoute,
    scoreUserPopupRoute
} from './';

const ENTITY_STATES = [...scoreUserRoute, ...scoreUserPopupRoute];

@NgModule({
    imports: [Peduc2SharedModule, Peduc2AdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ScoreUserComponent,
        ScoreUserDetailComponent,
        ScoreUserUpdateComponent,
        ScoreUserDeleteDialogComponent,
        ScoreUserDeletePopupComponent
    ],
    entryComponents: [ScoreUserComponent, ScoreUserUpdateComponent, ScoreUserDeleteDialogComponent, ScoreUserDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Peduc2ScoreUserModule {}
