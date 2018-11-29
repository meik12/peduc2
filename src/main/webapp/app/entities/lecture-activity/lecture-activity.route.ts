import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { LectureActivity } from 'app/shared/model/lecture-activity.model';
import { LectureActivityService } from './lecture-activity.service';
import { LectureActivityComponent } from './lecture-activity.component';
import { LectureActivityDetailComponent } from './lecture-activity-detail.component';
import { LectureActivityUpdateComponent } from './lecture-activity-update.component';
import { LectureActivityDeletePopupComponent } from './lecture-activity-delete-dialog.component';
import { ILectureActivity } from 'app/shared/model/lecture-activity.model';

@Injectable({ providedIn: 'root' })
export class LectureActivityResolve implements Resolve<ILectureActivity> {
    constructor(private service: LectureActivityService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((lectureActivity: HttpResponse<LectureActivity>) => lectureActivity.body));
        }
        return of(new LectureActivity());
    }
}

export const lectureActivityRoute: Routes = [
    {
        path: 'lecture-activity',
        component: LectureActivityComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LectureActivities'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lecture-activity/:id/view',
        component: LectureActivityDetailComponent,
        resolve: {
            lectureActivity: LectureActivityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LectureActivities'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lecture-activity/new',
        component: LectureActivityUpdateComponent,
        resolve: {
            lectureActivity: LectureActivityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LectureActivities'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lecture-activity/:id/edit',
        component: LectureActivityUpdateComponent,
        resolve: {
            lectureActivity: LectureActivityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LectureActivities'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lectureActivityPopupRoute: Routes = [
    {
        path: 'lecture-activity/:id/delete',
        component: LectureActivityDeletePopupComponent,
        resolve: {
            lectureActivity: LectureActivityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LectureActivities'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
