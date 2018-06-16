import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AttributionTacheComponent } from './attribution-tache.component';
import { AttributionTacheDetailComponent } from './attribution-tache-detail.component';
import { AttributionTachePopupComponent } from './attribution-tache-dialog.component';
import { AttributionTacheDeletePopupComponent } from './attribution-tache-delete-dialog.component';

export const attributionTacheRoute: Routes = [
    {
        path: 'attribution-tache',
        component: AttributionTacheComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.attributionTache.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'attribution-tache/:id',
        component: AttributionTacheDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.attributionTache.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attributionTachePopupRoute: Routes = [
    {
        path: 'attribution-tache-new',
        component: AttributionTachePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.attributionTache.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attribution-tache/:id/edit',
        component: AttributionTachePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.attributionTache.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attribution-tache/:id/delete',
        component: AttributionTacheDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.attributionTache.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
