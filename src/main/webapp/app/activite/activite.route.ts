/**
 * Created by sardehali on 02/03/18.
 */
/**
 * Created by sardehali on 02/03/18.
 */
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ActiviteComponent } from './activite.component';

export const ActiviteRoute: Routes = [
    {
        path: 'activite',
        component: ActiviteComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_CHEF_DE_PROJET', 'ROLE_DIRECTEUR_DE_PROJET','ROLE_USER'],
            pageTitle: 'progestoApp.projet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
