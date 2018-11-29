import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Peer } from 'app/shared/model/peer.model';
import { PeerService } from './peer.service';
import { PeerComponent } from './peer.component';
import { PeerDetailComponent } from './peer-detail.component';
import { PeerUpdateComponent } from './peer-update.component';
import { PeerDeletePopupComponent } from './peer-delete-dialog.component';
import { IPeer } from 'app/shared/model/peer.model';

@Injectable({ providedIn: 'root' })
export class PeerResolve implements Resolve<IPeer> {
    constructor(private service: PeerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((peer: HttpResponse<Peer>) => peer.body));
        }
        return of(new Peer());
    }
}

export const peerRoute: Routes = [
    {
        path: 'peer',
        component: PeerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'peer/:id/view',
        component: PeerDetailComponent,
        resolve: {
            peer: PeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'peer/new',
        component: PeerUpdateComponent,
        resolve: {
            peer: PeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'peer/:id/edit',
        component: PeerUpdateComponent,
        resolve: {
            peer: PeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const peerPopupRoute: Routes = [
    {
        path: 'peer/:id/delete',
        component: PeerDeletePopupComponent,
        resolve: {
            peer: PeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
