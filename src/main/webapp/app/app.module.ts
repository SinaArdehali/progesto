import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { ProgestoSharedModule, UserRouteAccessService } from './shared';
import { ProgestoHomeModule } from './home/home.module';
import { ProgestoAdminModule } from './admin/admin.module';
import { ProgestoAccountModule } from './account/account.module';
import { ProgestoEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';
//import { NG_TABLE_DIRECTIVES} from 'ng2-expanding-table';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';
import {ActiviteModule} from "./activite/activite.module";

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        ProgestoSharedModule,
        ProgestoHomeModule,
        ProgestoAdminModule,
        ProgestoAccountModule,
        ProgestoEntityModule,
        ActiviteModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class ProgestoAppModule {}
