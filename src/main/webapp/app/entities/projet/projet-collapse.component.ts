import {Component, OnInit, Input, OnDestroy} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import {Projet} from "./projet.model";
import {ProjetService} from "./projet.service";
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {Tache} from "../tache/tache.model";
import {TacheService} from "../tache/tache.service";
import {tachePopupRoute} from "../tache/tache.route";

@Component({
    selector: 'collapse-component',
    templateUrl: './projet-collapse.component.html',
    styleUrls: [
        'projet-collapse.component.css'
    ]
})



export class ProjetCollapsePopupComponent implements OnInit, OnDestroy{

    @Input () projet : Projet;
    tacheParProjets: Tache[];
    sumJoursVendus: number;
    currentAccount: any;
    eventSubscriber: Subscription;
    // show : boolean;
    show = false;


    constructor(
        private projetService: ProjetService,
        private tacheService: TacheService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll(){

        this.projetService.queryTachesParProjet(this.projet).subscribe(
            (res: ResponseWrapper) => {
                //this.tacheParProjets = res.json;
                this.tacheParProjets = res.json;


                //this.show = true;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
        // console.log("Dans loadAll  --2 this.tacheParProjets.length : " + this.tacheParProjets);
        // console.log(this.show);
        // console.log("jessaie dafficher le contenu de variable : " + variable.toString());
    }

    ngOnInit() {
        this.loadAll();
        //console.log("this.projet.id : " + this.projet.id);
        //console.log("this.tacheParProjets : " + JSON.stringify(this.tacheParProjets));
        //console.log("this.tacheParProjets : " + JSON.stringify({ data: this.tacheParProjets}, null, 4));
        //console.log("this.tacheParProjets : " + JSON.stringify(JSON.parse(this.tacheParProjets.toString())));

        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProjets();
        this.registerChangeInTaches();
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

    tacheByProjet(projet: Projet)
    {
        //this.variableBrouillon = projet.toString();

        // this.projetService.find(1151).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.projetBrouillons = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );

//je crois que le blic de code cidessous ne me sert a rien . je le laisse pour l'instant'
        this.projetService.queryTachesParProjet(projet).subscribe(
            (res: ResponseWrapper) => {
                this.tacheParProjets = res.json;

                console.log(this.tacheParProjets);
                //this.show = true;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
        console.log(this.show);

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


    projetSansTache(obj) {
        return (obj && (Object.keys(obj).length === 0));
    }
}
