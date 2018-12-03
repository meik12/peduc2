import { ScoreComponent } from './../entities/score/score.component';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Peduc2SharedModule } from '../shared';

import { PROFILE_ROUTE, ProfileComponent } from './';

@NgModule({
    imports: [Peduc2SharedModule, RouterModule.forRoot([PROFILE_ROUTE], { useHash: true })],
    declarations: [ProfileComponent, ScoreComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Peduc2AppProfileModule {}
