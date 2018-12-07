import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ScoreUser } from 'app/shared/model/score-user.model';
import { ScoreUserService } from './score-user.service';
import { ScoreUserComponent } from './score-user.component';
import { ScoreUserDetailComponent } from './score-user-detail.component';
import { ScoreUserUpdateComponent } from './score-user-update.component';
import { ScoreUserDeletePopupComponent } from './score-user-delete-dialog.component';
import { IScoreUser } from 'app/shared/model/score-user.model';

@Injectable({ providedIn: 'root' })
export class ScoreUserResolve implements Resolve<IScoreUser> {
    constructor(private service: ScoreUserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((scoreUser: HttpResponse<ScoreUser>) => scoreUser.body));
        }
        return of(new ScoreUser());
    }
}

export const scoreUserRoute: Routes = [
    {
        path: 'score-user',
        component: ScoreUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ScoreUsers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'score-user/:id/view',
        component: ScoreUserDetailComponent,
        resolve: {
            scoreUser: ScoreUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ScoreUsers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'score-user/new',
        component: ScoreUserUpdateComponent,
        resolve: {
            scoreUser: ScoreUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ScoreUsers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'score-user/:id/edit',
        component: ScoreUserUpdateComponent,
        resolve: {
            scoreUser: ScoreUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ScoreUsers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const scoreUserPopupRoute: Routes = [
    {
        path: 'score-user/:id/delete',
        component: ScoreUserDeletePopupComponent,
        resolve: {
            scoreUser: ScoreUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ScoreUsers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
