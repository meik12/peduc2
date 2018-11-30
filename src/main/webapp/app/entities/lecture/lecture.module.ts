import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Peduc2SharedModule } from 'app/shared';
import { Peduc2AdminModule } from 'app/admin/admin.module';
import {
    LectureComponent,
    LectureDetailComponent,
    LectureUpdateComponent,
    LectureDeletePopupComponent,
    LectureDeleteDialogComponent,
    lectureRoute,
    lecturePopupRoute
} from './';

const ENTITY_STATES = [...lectureRoute, ...lecturePopupRoute];

@NgModule({
    imports: [Peduc2SharedModule, Peduc2AdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [LectureDetailComponent, LectureUpdateComponent, LectureDeleteDialogComponent, LectureDeletePopupComponent],
    entryComponents: [LectureComponent, LectureUpdateComponent, LectureDeleteDialogComponent, LectureDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Peduc2LectureModule {}
