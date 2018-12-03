import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { ProfileComponent } from './';

export const PROFILE_ROUTE: Route = {
    path: 'profile',
    component: ProfileComponent,
    data: {
        authorities: [],
        pageTitle: 'profile.title'
    },
    canActivate: [UserRouteAccessService]
};
