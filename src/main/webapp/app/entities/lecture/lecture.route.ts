import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Lecture } from 'app/shared/model/lecture.model';
import { LectureService } from './lecture.service';
import { LectureComponent } from './lecture.component';
import { LectureDetailComponent } from './lecture-detail.component';
import { LectureUpdateComponent } from './lecture-update.component';
import { LectureDeletePopupComponent } from './lecture-delete-dialog.component';
import { ILecture } from 'app/shared/model/lecture.model';

@Injectable({ providedIn: 'root' })
export class LectureResolve implements Resolve<ILecture> {
    constructor(private service: LectureService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((lecture: HttpResponse<Lecture>) => lecture.body));
        }
        return of(new Lecture());
    }
}

export const lectureRoute: Routes = [
    {
        path: 'lecture',
        component: LectureComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lectures'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lecture/:id/view',
        component: LectureDetailComponent,
        resolve: {
            lecture: LectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lectures'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lecture/new',
        component: LectureUpdateComponent,
        resolve: {
            lecture: LectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lectures'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lecture/:id/edit',
        component: LectureUpdateComponent,
        resolve: {
            lecture: LectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lectures'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lecturePopupRoute: Routes = [
    {
        path: 'lecture/:id/delete',
        component: LectureDeletePopupComponent,
        resolve: {
            lecture: LectureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lectures'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
