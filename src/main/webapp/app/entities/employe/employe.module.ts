import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProgestoSharedModule } from '../../shared';
import { ProgestoAdminModule } from '../../admin/admin.module';
import {
    EmployeService,
    EmployePopupService,
    EmployeComponent,
    EmployeDetailComponent,
    EmployeDialogComponent,
    EmployePopupComponent,
    EmployeDeletePopupComponent,
    EmployeDeleteDialogComponent,
    employeRoute,
    employePopupRoute,
} from './';

const ENTITY_STATES = [
    ...employeRoute,
    ...employePopupRoute,
];

@NgModule({
    imports: [
        ProgestoSharedModule,
        ProgestoAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EmployeComponent,
        EmployeDetailComponent,
        EmployeDialogComponent,
        EmployeDeleteDialogComponent,
        EmployePopupComponent,
        EmployeDeletePopupComponent,
    ],
    entryComponents: [
        EmployeComponent,
        EmployeDialogComponent,
        EmployePopupComponent,
        EmployeDeleteDialogComponent,
        EmployeDeletePopupComponent,
    ],
    providers: [
        EmployeService,
        EmployePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProgestoEmployeModule {}
