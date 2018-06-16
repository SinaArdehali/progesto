import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProgestoSharedModule } from '../../shared';
import { ProgestoAdminModule } from '../../admin/admin.module';
import {
    AttributionTacheService,
    AttributionTachePopupService,
    AttributionTacheComponent,
    AttributionTacheDetailComponent,
    AttributionTacheDialogComponent,
    AttributionTachePopupComponent,
    AttributionTacheDeletePopupComponent,
    AttributionTacheDeleteDialogComponent,
    attributionTacheRoute,
    attributionTachePopupRoute,
} from './';

const ENTITY_STATES = [
    ...attributionTacheRoute,
    ...attributionTachePopupRoute,
];

@NgModule({
    imports: [
        ProgestoSharedModule,
        ProgestoAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AttributionTacheComponent,
        AttributionTacheDetailComponent,
        AttributionTacheDialogComponent,
        AttributionTacheDeleteDialogComponent,
        AttributionTachePopupComponent,
        AttributionTacheDeletePopupComponent,
    ],
    entryComponents: [
        AttributionTacheComponent,
        AttributionTacheDialogComponent,
        AttributionTachePopupComponent,
        AttributionTacheDeleteDialogComponent,
        AttributionTacheDeletePopupComponent,
    ],
    providers: [
        AttributionTacheService,
        AttributionTachePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProgestoAttributionTacheModule {}
