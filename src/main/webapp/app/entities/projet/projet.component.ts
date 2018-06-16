import {Component, OnInit, Input, OnDestroy} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Projet } from './projet.model';
import { ProjetService } from './projet.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import {TacheService} from "../tache/tache.service";
import {Tache} from "../tache/tache.model";

import {tachePopupRoute} from "../tache/tache.route";


import { ViewChild } from '@angular/core';
import { DataTable, DataTableTranslations, DataTableResource } from 'angular-4-data-table-bootstrap-4';
import { films } from './projet.service';


@Component({
    selector: 'jhi-projet',
    templateUrl: './projet.component.html',
    styleUrls: [
        'projet.component.css'
    ]
})
export class ProjetComponent implements OnInit, OnDestroy {
    projets: Projet[];
    taches: Tache[];
    tacheParProjets: Tache[];
    tacheParProjetsBISs: Tache[];
    currentAccount: any;
    eventSubscriber: Subscription;
    variableBrouillon: String ;
//    projetBrouillons : Projet;

    // import de lautre fichier:
    // @Input () projet : Projet;
    sumJoursVendus: number;
    // show : boolean;
    show = false;
    varContenantId: number;

    recupererTousTacheAvecDonneess: Tache[];

rotation :boolean;




    projetResource = new DataTableResource(this.projets);
    filmResource = new DataTableResource(films);
    films = [];
    filmCount = 0;
    projetCount = 0;

    @ViewChild(DataTable) projetsTable;



    // active:boolean = false;
    //
    // frontPath:string = "./pxdbypisezfowvsmgaoh.png";
    //
    // backPath:string = "http://tophondacars.com/wp-content/uploads/2018-Honda-Civic-Type-R-pwe.jpg";



    constructor(
        private projetService: ProjetService,
        //ci dessous , rajouté par moi.
        private tacheService: TacheService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
        this.projetResource.count().then(count => this.projetCount = count);
    }

    loadAll(){
        this.projetService.query().subscribe(
            (res: ResponseWrapper) => {
                this.projets = res.json;
                // jitere a travers ce tableau dobjet projet pour essayer de mettre tous les
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );


        this.tacheService.query().subscribe(
            (res: ResponseWrapper) => {
                this.taches = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );



        // this.tacheService.queryTachesParProjet().subscribe(
        //     (res: ResponseWrapper) => {
        //         this.tacheParProjets = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );


        // import de lature fichier
        //console.log(" voici le ocntenu de this.projets:" + this.projets.toString())
        // this.projetService.queryTachesParProjet(this.projet).subscribe(
        //     (res: ResponseWrapper) => {
        //         //this.tacheParProjets = res.json;
        //         this.tacheParProjets = res.json;
        //
        //
        //         //this.show = true;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );


        // console.log("Dans loadAll  --2 this.tacheParProjets.length : " + this.tacheParProjets);
        // console.log(this.show);
        // console.log("jessaie dafficher le contenu de variable : " + variable.toString());

    }
    ngOnInit() {
        this.loadAll();
        //console.log("Dans loadAll  --2 this.tacheParProjets.length : " + this.tacheParProjets);
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProjets();
        //ci dessous , rajouté par moi.
        this.registerChangeInTaches();

        // for(let projet of this.projets) {
        //     for(let test2 of projet.ta)
        // }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Projet) {
        return item.id;
    }
    registerChangeInProjets() {
        this.eventSubscriber = this.eventManager.subscribe('projetListModification', (response) => this.loadAll());
    }

    //ci dessous , rajouté par moi.
    registerChangeInTaches() {
        this.eventSubscriber = this.eventManager.subscribe('tacheListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    // tacheByProjet(projet: Projet)
    // {
    //     this.variableBrouillon = projet.toString();
    //
    //     // this.projetService.find(1151).subscribe(
    //     //     (res: ResponseWrapper) => {
    //     //         this.projetBrouillons = res.json;
    //     //     },
    //     //     (res: ResponseWrapper) => this.onError(res.json)
    //     // );
    //
    //
    //     this.projetService.queryTachesParProjet(projet).subscribe(
    //         (res: ResponseWrapper) => {
    //             this.tacheParProjets = res.json;
    //         },
    //         (res: ResponseWrapper) => this.onError(res.json)
    //     );
    //     console.log(this.tacheParProjets);
    //     alert(this.variableBrouillon);
    //     alert(this.tacheParProjets);
    // }

    tacheByProjet(projet: Projet)
    {
//this.rotation = true;
        //this.variableBrouillon = projet.toString();

        // this.projetService.find(1151).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.projetBrouillons = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );

//je crois que le blic de code cidessous ne me sert a rien . je le laisse pour l'instant'
        this.projetService.queryTachesParProjetBIS(projet).subscribe(
            (res: ResponseWrapper) => {
                this.tacheParProjets = null;
                this.tacheParProjets = res.json;
console.log("impression res.json  :::: " + res.json);
                console.log("impression de mon this.tacheParProjets : " + this.tacheParProjets);
                //this.show = true;
                this.varContenantId = projet.id;
                console.log("this.varContenantId danns le subscribe: " + this.varContenantId);


            },
            (res: ResponseWrapper) => this.onError(res.json)
        );



        this.projetService.queryTachesParProjetBIS(projet).subscribe(
            (res: ResponseWrapper) => {
                this.tacheParProjetsBISs = res.json;
                console.log("voici mon tacheParProjetsBIS nouvelle ++++++++++++: " + JSON.stringify(this.tacheParProjetsBISs));
                //this.show = true;
                this.varContenantId = projet.id;
                console.log("this.varContenantId danns le subscribe: " + this.varContenantId);


            },
            (res: ResponseWrapper) => this.onError(res.json)
        );




        console.log(this.show);
        console.log("this.varContenantId exterieur subscribe: " + this.varContenantId);




        // this.projetService.recupererTousTacheAvecDonnees(projet.id).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.recupererTousTacheAvecDonneess = res.json;
        //         console.log("voici la valeur de mon tableau de tache this.recupererTousTacheAvecDonneess : " + this.recupererTousTacheAvecDonneess);
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );


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

    refresh(): void {
        window.location.reload();
    }

    projetSansTache(obj) {
        return (obj && (Object.keys(obj).length === 0));
    }














    reloadProjets(params) {
        this.projetResource.query(params).then(projets => this.projets = projets);
    }

    cellColor(car) {
        return 'rgb(255, 255,' + (155 + Math.floor(100 - ((car.rating - 8.7)/1.3)*100)) + ')';
    };

    // special params:
    translations = <DataTableTranslations>{
        indexColumn: 'Index column',
        expandColumn: 'Expand column',
        selectColumn: 'Select column',
        paginationLimit: 'Max results',
        paginationRange: 'Result range'
    };






}
