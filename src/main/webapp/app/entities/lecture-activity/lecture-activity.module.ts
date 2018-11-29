import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Peduc2SharedModule } from 'app/shared';
import { Peduc2AdminModule } from 'app/admin/admin.module';
import {
    LectureActivityComponent,
    LectureActivityDetailComponent,
    LectureActivityUpdateComponent,
    LectureActivityDeletePopupComponent,
    LectureActivityDeleteDialogComponent,
    lectureActivityRoute,
    lectureActivityPopupRoute
} from './';

const ENTITY_STATES = [...lectureActivityRoute, ...lectureActivityPopupRoute];

@NgModule({
    imports: [Peduc2SharedModule, Peduc2AdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        LectureActivityComponent,
        LectureActivityDetailComponent,
        LectureActivityUpdateComponent,
        LectureActivityDeleteDialogComponent,
        LectureActivityDeletePopupComponent
    ],
    entryComponents: [
        LectureActivityComponent,
        LectureActivityUpdateComponent,
        LectureActivityDeleteDialogComponent,
        LectureActivityDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Peduc2LectureActivityModule {}
