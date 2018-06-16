import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';


import { ProgestoProjetModule } from './projet/projet.module';
import { ProgestoTacheModule } from './tache/tache.module';
import { ProgestoAttributionTacheModule } from './attribution-tache/attribution-tache.module';
import {ProgestoEmployeModule} from "./employe/employe.module";
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ProgestoProjetModule,
        ProgestoTacheModule,
        ProgestoAttributionTacheModule,
        ProgestoEmployeModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProgestoEntityModule {}
