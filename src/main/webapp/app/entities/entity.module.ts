import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { Peduc2PeerModule } from './peer/peer.module';
import { Peduc2SuggestedLectureModule } from './suggested-lecture/suggested-lecture.module';
import { Peduc2LectureActivityModule } from './lecture-activity/lecture-activity.module';
import { Peduc2LectureModule } from './lecture/lecture.module';
import { Peduc2ScoreModule } from './score/score.module';
import { Peduc2ProfileModule } from './profile/profile.module';
import { Peduc2ScoreUserModule } from './score-user/score-user.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        Peduc2PeerModule,
        Peduc2SuggestedLectureModule,
        Peduc2LectureActivityModule,
        Peduc2LectureModule,
        Peduc2ScoreModule,
        Peduc2ProfileModule,
        Peduc2ScoreUserModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Peduc2EntityModule {}
