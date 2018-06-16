import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { Response } from '@angular/http';

import {Observable, Subscription} from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Tache } from './tache.model';
import { Projet} from '../projet/projet.model';
import { TachePopupService } from './tache-popup.service';
import { TacheService } from './tache.service';
import { ProjetService } from '../projet/projet.service';
import { ResponseWrapper } from '../../shared';
import { AttributionTacheService} from '../attribution-tache/attribution-tache.service';
import { AttributionTache} from '../attribution-tache/attribution-tache.model';
import { Compiler } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Inject } from '@angular/core';

@Component({
    selector: 'jhi-tache-dialog',
    templateUrl: './tache-dialog.component.html',
    styleUrls: [
        './tache-dialog.component.css'
    ]
})
export class TacheDialogComponent implements OnInit {

    tache: Tache;
    isSaving: boolean;

    projets: Projet[];
    debutTacheDp: any;
    finTacheDp: any;
    resultDuReturnModifierJourVendus: Tache;
    isReadOnly: boolean;

    messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat: number;


    eventSubscriber: Subscription;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private tacheService: TacheService,
        private projetService: ProjetService,
        private attributionTacheService: AttributionTacheService,
        private eventManager: JhiEventManager,
        private router: Router,
        private _compiler: Compiler,
        @Inject(DOCUMENT) private document: Document
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projetService.query()
            .subscribe((res: ResponseWrapper) => { this.projets = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        if (this.tache.nomTache!=null)
        {
            this.isReadOnly=true;
        }
        this.registerChangeInProjets();
        //ci dessous , rajouté par moi.
        this.registerChangeInTaches();
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {

        this.isSaving = true;
        if (this.tache.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tacheService.update(this.tache));
                // this.router.navigateByUrl('/projet');
            // console.log("this.router.navigateByUrl('/projet');")

            // var childWindow = "";
            // var newTabUrl="/projet";
            // location.href=newTabUrl
            //

            // var curr_page = window.location.href,
            //
            // next_page = curr_page+"&action=someaction";
            //
            // window.location = next_page;

            //location.reload();

            // console.log(" window.location.href : " + window.location.href);



            console.log("this.document.location.href : " + window.location.origin);
            console.log("this.document.location.href nouvelle : " +  window.location.origin+'/'+'projet');
            location.href= window.location.origin+'/#/projet';
            location.reload();





            // this._compiler.clearCache();
            // location.replace("/#/projet");

        } else {
            this.subscribeToSaveResponse(
                this.tacheService.create(this.tache));
            location.href= window.location.origin+'/#/projet';
            location.reload();
        }



        // a ce moment la, on doit balancer un systeme de mise a jour
        // du nombre de jours par projet en faisant la somme des
        // jours des taches qui la compose.


// JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR A PRESENT LA MAJ DU PROJET EN MEME TEMPS QUE LA MAJ DE LA TACHE'
        // this.subscribeToSaveResponse2(
        //     this.tacheService.majJoursVendProj(this.tache));

        // ci dessous, cree par moiSina
        // if (this.tache.joursVendusTache != null)
        // {
        //     for(var i = 0;i<this.tache.joursVendusTache;i++) {
        //         let attributionTache: AttributionTache;
        //         this.subscribeToSaveResponse(
        //             this.attributionTacheService.create(attributionTache));
        //     }
        // }
    }

    // redirige(){
    //     alert("redirigie");
    //     this.router.navigate(['/employe']);
    // }

    registerChangeInProjets() {
        this.eventSubscriber = this.eventManager.subscribe('projetListModification', (response) => this.ngOnInit());
    }

    //ci dessous , rajouté par moi.
    registerChangeInTaches() {
        this.eventSubscriber = this.eventManager.subscribe('tacheListModification', (response) => this.ngOnInit());
    }


    private subscribeToSaveResponse(result: Observable<Tache>) {
        result.subscribe((res: Tache) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    // private subscribeToSaveResponse2(result: Observable<Projet>) {
    //     // console.log(JSON.stringify(Projet));
    //     // JSON.stringify({ data: Projet}, null, 4);
    //     result.subscribe((res: Projet) =>
    //         this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    // }

    //
    // private onSaveSuccess2(result: Projet) {
    //     this.eventManager.broadcast({name: 'projetListModification', content: 'OK'});
    //     this.isSaving = false;
    //     this.activeModal.dismiss(result);
    //}

    private onSaveSuccess(result: Tache) {
        this.eventManager.broadcast({ name: 'tacheListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackProjetById(index: number, item: Projet) {
        return item.id;
    }

    modifierJoursVendus(nbJour: number, ajouterOuRetirer: boolean)
    {

        console.log(" voici la tache.nbJour : " + nbJour + " ------ " + ajouterOuRetirer);
        if (nbJour > 0  && ajouterOuRetirer){

            console.log(this.tache.id);
            console.log("ajouterOuRetirer : " + ajouterOuRetirer);

            this.tacheService.ajouterJoursVendus(nbJour, this.tache.id).subscribe(
                    (res: ResponseWrapper) => {
                        this.resultDuReturnModifierJourVendus = res.json;

                    },
                    (res: ResponseWrapper) => this.onError(res.json)
                );

            alert("Vous venez d'ajouter " + nbJour + " jours à cette tâche.");


            this.tache.joursVendusTache += nbJour;
            this.save();

            this.ngOnInit();
        }
        else{

            console.log(this.tache.id);


            this.tacheService.selectPuisRetirerJoursVendus(nbJour, this.tache.id).subscribe(
                (res: ResponseWrapper) => {
                    this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat = res.json;
                    console.log("€€€€€€€€€€ this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat : " + this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat);

                    console.log("valeurs recherchees" + this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat + " " + nbJour);
                    if (this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat != nbJour){
                        alert("Attention! Vous avez tenté de retirer " + nbJour + " jours à cette tâche. Cependant, nous ne pouvons retirer que des jours qui ne sont pas attribués aux ingénieurs. Ainsi, nous avons retiré "+ this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat +" jours. Il s'agit du nombre de jours qui etaient disponibles, c'est a dire non attribués. Votre demande n'a donc pas pu être totalement satisfaite.");
                    }
                    else {
                        alert("Vous venez de retirer " + nbJour + " jours à cette tâche.");

                        // this.redirige();
                    }
                },
                (res: ResponseWrapper) => this.onError(res.json)
            );

            // this.tache.joursVendusTache -= this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat;

            this.tache.joursVendusTache -= nbJour;
            this.save();

            this.ngOnInit();
        }
    }


}

@Component({
    selector: 'jhi-tache-popup',
    template: ''
})
export class TachePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tachePopupService: TachePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {

            if ( params['id'] ) {
                this.tachePopupService
                    .open(TacheDialogComponent as Component, params['id']);
            }
            else if ( params['idProjetMere']){
                console.log("voici lidProjetMere : " + params['idProjetMere']);
                console.log(this.tachePopupService
                    .open2(TacheDialogComponent as Component, params['idProjetMere']));
            }
            else {
                this.tachePopupService
                    .open(TacheDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
