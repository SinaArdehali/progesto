import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { EmployeComponent } from './employe.component';
import { EmployeDetailComponent } from './employe-detail.component';
import { EmployePopupComponent } from './employe-dialog.component';
import { EmployeDeletePopupComponent } from './employe-delete-dialog.component';

export const employeRoute: Routes = [
    {
        path: 'employe',
        component: EmployeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'employe/:id',
        component: EmployeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const employePopupRoute: Routes = [
    {
        path: 'employe-new',
        component: EmployePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employe/:id/edit',
        component: EmployePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employe/:id/delete',
        component: EmployeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'progestoApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
