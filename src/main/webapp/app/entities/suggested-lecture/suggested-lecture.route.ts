import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SuggestedLecture } from 'app/shared/model/suggested-lecture.model';
import { SuggestedLectureService } from './suggested-lecture.service';
import { SuggestedLectureComponent } from './suggested-lecture.component';
import { SuggestedLectureDetailComponent } from './suggested-lecture-detail.component';
import { SuggestedLectureUpdateComponent } from './suggested-lecture-update.component';
import { SuggestedLectureDeletePopupComponent } from './suggested-lecture-delete-dialog.component';
import { ISuggestedLecture } from 'app/shared/model/suggested-lecture.model';

@Injectable({ providedIn: 'root' })
export class SuggestedLectureResolve implements Resolve<ISuggestedLecture> {
    constructor(private service: SuggestedLectureService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((suggestedLecture: HttpResponse<SuggestedLecture>) => suggestedLecture.body));
        }
        return of(new SuggestedLecture());
    }
}

export const suggestedLectureRoute: Routes = [
    {
        path: 'suggested-lecture',
        component: SuggestedLectureComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SuggestedLectures'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'suggested-lecture/:id/view',
        component: SuggestedLectureDetailComponent,
        resolve: {
            suggestedLecture: SuggestedLectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SuggestedLectures'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'suggested-lecture/new',
        component: SuggestedLectureUpdateComponent,
        resolve: {
            suggestedLecture: SuggestedLectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SuggestedLectures'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'suggested-lecture/:id/edit',
        component: SuggestedLectureUpdateComponent,
        resolve: {
            suggestedLecture: SuggestedLectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SuggestedLectures'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const suggestedLecturePopupRoute: Routes = [
    {
        path: 'suggested-lecture/:id/delete',
        component: SuggestedLectureDeletePopupComponent,
        resolve: {
            suggestedLecture: SuggestedLectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SuggestedLectures'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
