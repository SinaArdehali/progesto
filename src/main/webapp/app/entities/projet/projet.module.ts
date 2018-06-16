import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProgestoSharedModule } from '../../shared';
import { ProgestoAdminModule } from '../../admin/admin.module';



import {DataTableModule } from 'angular-4-data-table-bootstrap-4';

import {
    ProjetService,
    ProjetPopupService,
    ProjetComponent,
    ProjetDetailComponent,
    ProjetDialogComponent,
    ProjetPopupComponent,
    ProjetDeletePopupComponent,
    ProjetDeleteDialogComponent,
    projetRoute,
    projetPopupRoute,
    ProjetCollapsePopupComponent,
} from './';

const ENTITY_STATES = [
    ...projetRoute,
    ...projetPopupRoute,
];

@NgModule({
    imports: [
        ProgestoSharedModule,
        ProgestoAdminModule,
        DataTableModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProjetComponent,
        ProjetDetailComponent,
        ProjetDialogComponent,
        ProjetDeleteDialogComponent,
        ProjetPopupComponent,
        ProjetDeletePopupComponent,
        ProjetCollapsePopupComponent
    ],
    entryComponents: [
        ProjetComponent,
        ProjetDialogComponent,
        ProjetPopupComponent,
        ProjetDeleteDialogComponent,
        ProjetDeletePopupComponent,
        ProjetCollapsePopupComponent
    ],
    providers: [
        ProjetService,
        ProjetPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProgestoProjetModule {}
