import {Component, OnInit, Input, OnDestroy} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import {Projet} from "../../../projet/projet.model";
import {ProjetService} from "../../../projet/projet.service";
import {Tache} from "../../tache.model";
import {TacheService} from "../../tache.service";
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../../../shared';
import {User} from "../../../../shared/user/user.model";

@Component({
    selector: 'button-collapse-component',
    templateUrl: './button-collapse.component.html'
})



export class ButtonAffecterCollapseComponent implements OnInit, OnDestroy{

    @Input () personnesNonAffectees : User;
    tacheParProjets: Tache[];
    sumJoursVendus: number;
    currentAccount: any;
    eventSubscriber: Subscription;


    constructor(
        private projetService: ProjetService,
        private tacheService: TacheService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll(){
        // this.projetService.queryTachesParProjet(this.projet).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.tacheParProjets = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );
    }

    ngOnInit() {
        // this.loadAll();
        // console.log(this.projet.id);
        // console.log(this.tacheParProjets);
        // this.principal.identity().then((account) => {
        //     this.currentAccount = account;
        // });
        // this.registerChangeInProjets();
        // this.registerChangeInTaches();
    };

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Projet) {
        return item.id;
    }
    registerChangeInProjets() {
        this.eventSubscriber = this.eventManager.subscribe('projetListModification', (response) => this.loadAll());
    }

    registerChangeInTaches() {
        this.eventSubscriber = this.eventManager.subscribe('tacheListModification', (response) => this.loadAll());
    }



    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    tacheByProjet(personnesNonAffectees: User)
    {
        //this.variableBrouillon = projet.toString();

        // this.projetService.find(1151).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.projetBrouillons = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );

// REMETTRE LE CODE CI DESSOUS DES QUE POSSIBLE
//         this.projetService.queryTachesParProjet(projet).subscribe(
//             (res: ResponseWrapper) => {
//                 this.tacheParProjets = res.json;
//             },
//             (res: ResponseWrapper) => this.onError(res.json)
//         );


        // console.log(this.tacheParProjets);
        // //alert(this.variableBrouillon);
        // alert(this.tacheParProjets);

        // JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR LE BLOC CI DESSOUS CORRESPOND A LA LIGNE TOTAL QUE J'AI INSERE ET QUI SE DEPLOYAIT QUAND ON CLIQUAIT SUR "VOIR LES TACHES DU PROJET"
        // this.projetService.sumJoursVendus(projet.id).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.sumJoursVendus = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );
    }

}
