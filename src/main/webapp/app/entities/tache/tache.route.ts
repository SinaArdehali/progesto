import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TacheComponent } from './tache.component';
import { TacheDetailComponent } from './tache-detail.component';
import { TacheAffectationComponent } from './tache-affectation.component';
import { TachePopupComponent } from './tache-dialog.component';
import { TacheDeletePopupComponent } from './tache-delete-dialog.component';
import {
    NouvelleAffectationTachePopupComponent,
    NouvelleAffectationDialogComponent
} from "./nouvelle-affectation/nouvelle-affectation-dialog.component";

export const tacheRoute: Routes = [
    {
        path: 'tache',
        component: TacheComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.tache.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tache/:id',
        component: TacheDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.tache.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tacheAffectation/:id',
        component: TacheAffectationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.tache.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tachePopupRoute: Routes = [
    {
        path: 'tache-new',
        component: TachePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.tache.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
//    ci dessous, c'est ce que j'ai rajoute
    {
        path: 'tache-new/:idProjetMere',
        component: TachePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.tache.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tache/:id/edit',
        component: TachePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.tache.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tache/:id/delete',
        component: TacheDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.tache.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'affectation-tache-new/:id',
        component: NouvelleAffectationTachePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.attributionTache.nouvelleAttributionTache.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }


];
