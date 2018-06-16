import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProgestoSharedModule } from '../../shared';
import {
    TacheService,
    TachePopupService,
    TacheComponent,
    TacheDetailComponent,
    TacheDialogComponent,
    TachePopupComponent,
    TacheDeletePopupComponent,
    TacheDeleteDialogComponent,
    tacheRoute,
    tachePopupRoute,
    TacheAffectationComponent,
    NouvelleAffectationDialogComponent,
    ButtonAffecterCollapseComponent
} from './';
import {NouvelleAffectationTachePopupComponent} from "./nouvelle-affectation/nouvelle-affectation-dialog.component";
//import { NouvelleAffectationDialogComponent} from './nouvelle-affectation/';

const ENTITY_STATES = [
    ...tacheRoute,
    ...tachePopupRoute,
];

@NgModule({
    imports: [
        ProgestoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TacheComponent,
        TacheDetailComponent,
        TacheDialogComponent,
        TacheDeleteDialogComponent,
        TachePopupComponent,
        TacheDeletePopupComponent,
        TacheAffectationComponent,
        NouvelleAffectationDialogComponent,
        NouvelleAffectationTachePopupComponent,
        ButtonAffecterCollapseComponent
    ],
    entryComponents: [
        TacheComponent,
        TacheDialogComponent,
        TachePopupComponent,
        TacheDeleteDialogComponent,
        TacheDeletePopupComponent,
        TacheAffectationComponent,
        NouvelleAffectationDialogComponent,
        NouvelleAffectationTachePopupComponent,
        ButtonAffecterCollapseComponent
    ],
    providers: [
        TacheService,
        TachePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProgestoTacheModule {}
