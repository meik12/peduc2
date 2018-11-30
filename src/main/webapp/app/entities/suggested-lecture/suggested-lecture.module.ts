import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Peduc2SharedModule } from 'app/shared';
import { Peduc2AdminModule } from 'app/admin/admin.module';
import {
    SuggestedLectureComponent,
    SuggestedLectureDetailComponent,
    SuggestedLectureUpdateComponent,
    SuggestedLectureDeletePopupComponent,
    SuggestedLectureDeleteDialogComponent,
    suggestedLectureRoute,
    suggestedLecturePopupRoute
} from './';

const ENTITY_STATES = [...suggestedLectureRoute, ...suggestedLecturePopupRoute];

@NgModule({
    imports: [Peduc2SharedModule, Peduc2AdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SuggestedLectureComponent,
        SuggestedLectureDetailComponent,
        SuggestedLectureUpdateComponent,
        SuggestedLectureDeleteDialogComponent,
        SuggestedLectureDeletePopupComponent
    ],
    entryComponents: [
        SuggestedLectureComponent,
        SuggestedLectureUpdateComponent,
        SuggestedLectureDeleteDialogComponent,
        SuggestedLectureDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Peduc2SuggestedLectureModule {}
