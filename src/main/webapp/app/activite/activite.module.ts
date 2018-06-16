import {CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ProgestoSharedModule} from "../shared/shared.module";
import {ProgestoAdminModule} from "../admin/admin.module";
import {ActiviteService} from "./activite.service";


import { FormsModule } from '@angular/forms';

import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { NgModule, ApplicationRef } from '@angular/core';

import {
    removeNgStyles,
    createNewHosts,
    createInputTransfer
} from '@angularclass/hmr';
import { RouterModule, PreloadAllModules } from '@angular/router';

import { ENV_PROVIDERS } from './environment';
import { ActiviteRoute } from './activite.route';
// App is our top level component
import { ActiviteComponent } from './activite.component';
import { APP_RESOLVER_PROVIDERS } from './app.resolver';
import {InternalStateType } from './activite.service';
import { NoContentComponent } from './no-content';
// import { XLargeDirective } from './home/x-large';



// Application wide providers
const APP_PROVIDERS = [...APP_RESOLVER_PROVIDERS, ActiviteService];

type StoreType = {
    state: InternalStateType;
    restoreInputValues: () => void;
    disposeOldHosts: () => void;
};




const ENTITY_STATES = [
    ...ActiviteRoute
];


@NgModule({

    bootstrap: [ActiviteComponent],
    declarations: [ActiviteComponent, NoContentComponent],

  imports: [
    CommonModule,
      ProgestoSharedModule,
      ProgestoAdminModule,
      FormsModule,
      BrowserModule,
      ReactiveFormsModule,
      HttpModule,
      RouterModule.forRoot(ENTITY_STATES, { useHash: true })
  ],

    entryComponents: [
        ActiviteComponent
    ],
    providers: [
        [ENV_PROVIDERS, APP_PROVIDERS]
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ActiviteModule {


    constructor(public appRef: ApplicationRef, public appState: ActiviteService) {}

    public hmrOnInit(store: StoreType) {
        if (!store || !store.state) {
            return;
        }
        console.log('HMR store', JSON.stringify(store, null, 2));
        /**
         * Set state
         */
        this.appState._state = store.state;
        /**
         * Set input values
         */
        if ('restoreInputValues' in store) {
            let restoreInputValues = store.restoreInputValues;
            setTimeout(restoreInputValues);
        }

        this.appRef.tick();
        delete store.state;
        delete store.restoreInputValues;
    }

    public hmrOnDestroy(store: StoreType) {
        const cmpLocation = this.appRef.components.map(
            cmp => cmp.location.nativeElement
        );
        /**
         * Save state
         */
        const state = this.appState._state;
        store.state = state;
        /**
         * Recreate root elements
         */
        store.disposeOldHosts = createNewHosts(cmpLocation);
        /**
         * Save input values
         */
        store.restoreInputValues = createInputTransfer();
        /**
         * Remove styles
         */
        removeNgStyles();
    }

    public hmrAfterDestroy(store: StoreType) {
        /**
         * Display new elements
         */
        store.disposeOldHosts();
        delete store.disposeOldHosts;
    }









}
