//sauvegarde de mon nouvelle-affectation-dialog.component.ts


/**
 * Created by sardehali on 31/01/18.
 */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import {Observable, Subscription} from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Tache } from '../tache.model';
import { Projet} from '../../projet/projet.model';
import { TachePopupService } from '../tache-popup.service';
import { TacheService } from '../tache.service';
import { ProjetService } from '../../projet/projet.service';
import { ResponseWrapper } from '../../../shared';
import { AttributionTacheService} from '../../attribution-tache/attribution-tache.service';
import { AttributionTache} from '../../attribution-tache/attribution-tache.model';
import {Principal} from "../../../shared/auth/principal.service";
import {User} from "../../../shared/user/user.model";
import {UserService} from "../../../shared/user/user.service";
import {EmployeService} from "../../employe/employe.service";
import {NgForm, Validators, FormControl, FormArray, FormGroup} from "@angular/forms";
//import {ParticTacheDTO} from "../ParticTacheDTO.model";

@Component({
    selector: 'jhi-nouvelle-affectation-dialog',
    templateUrl: './nouvelle-affectation-dialog.component.html',
    styleUrls: [
        './nouvelle-affectation-dialog.component.css'
    ]
})


export class NouvelleAffectationDialogComponent implements OnInit {

    employesAvecMs: User[];
    //ingenieursParTache: User[];
    participants: User[];
    personnesNonAffecteess: User[];
    //participants2:ParticTacheDTO[];
    nombreDeJoursAttribuesVar: number;
    private subscription: Subscription;
//    donc subscription correspond à  l'id que j'ai recupere dans le route
    currentAccount: any;
    private eventSubscriber: Subscription;

    tache: Tache;
    isSaving: boolean;

    listeAttribTaches: AttributionTache[];
    nombreMaxJours: number;

    projets: Projet[];
    debutTacheDp: any;
    finTacheDp: any;
    idValue: any;
idIngeSansAffectation: number;
    idIngeSansAffecTemp: number;
    getAllIngeNoAttribs: String[];
    nbJourSansAffec: number;


    constructor(
        private employeService: EmployeService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private tacheService: TacheService,
        private projetService: ProjetService,
        private route: ActivatedRoute,
        private userService: UserService,
        private attributionTacheService: AttributionTacheService
    ) {
    }

    ngOnInit() {
        // console.log("dans nouvelle affectation");
        //this.nombreDeJoursAttribuesMeth(11452,2101);

        // this.isSaving = false;
        // this.projetService.query()
        //     .subscribe((res: ResponseWrapper) => { this.projets = res.json; }, (res: ResponseWrapper) => this.onError(res.json));

        // rajoute par moiSina
        // console.log(this.route.params);
        console.log("JSON.stringify(this.tache)) : " + JSON.stringify(this.tache));
        this.idValue = this.tache.id;
        console.log("voici l'id de la tache " + this.idValue);

        this.subscription = this.route.params.subscribe((params) => {
            //console.log(" params['id'] :: " + params['id']);
            // console.log("params :: " + JSON.stringify(params));
            console.log("on est ici dans le component du haut NouvelleAffectationDialogComponent" + JSON.stringify(params));
            // console.log(" ludni matin params :: " + JSON.stringify(params));
            this.load(this.idValue);
        });
        // console.log("ci dessous, le param. contient il l'id?")
        // console.log(this.route.params);
        this.registerChangeInTaches();
        this.registerChangeInNombreJours();
    }

    load(id) {
        // console.log("dans le load");
        // a ce niveau, id est undefined
// console.log(id);
        this.tacheService.find(id);
        // this.tacheService.find(id).subscribe((tache) => {
        //     this.tache = tache;
        //     console.log("dans mon load de nouvelle tache affectation");
        //     console.log(this.tache);
        // });


        // this.userService.queryPersonnesNonAffectees().subscribe(
        //     (res: ResponseWrapper) => {
        //         this.personnesNonAffecteess = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );

        this.employeService.getAllIngeM().subscribe(
            (res: ResponseWrapper) => {
                this.employesAvecMs = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );

        // this.projetService.query().subscribe(
        //     (res: ResponseWrapper) => {
        //         this.projets = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );


        //
        // this.attributionTacheService.getAttrTacheLimit(id, 3, 5).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.listeAttribTaches = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );

        this.attributionTacheService.getMaxJours(id).subscribe(
            (res: ResponseWrapper) => {
                this.nombreMaxJours = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
        // this.registerChangeInNombreJours();


        this.employeService.getAllIngeNoAttrib().subscribe(
            (res: ResponseWrapper) => {
                this.getAllIngeNoAttribs = res.json;
                console.log("this.getAllIngeNoAttribs.toString() : "+ JSON.stringify(this.getAllIngeNoAttribs));
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );


    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

//     save() {
//
//         this.isSaving = true;
//         if (this.tache.id !== undefined) {
//             this.subscribeToSaveResponse(
//                 this.tacheService.update(this.tache));
//         } else {
//             this.subscribeToSaveResponse(
//                 this.tacheService.create(this.tache));
//         }
//         // a ce moment la, on doit balancer un systeme de mise a jour
//         // du nombre de jours par projet en faisant la somme des
//         // jours des taches qui la compose.
//
//
// // JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR A PRESENT LA MAJ DU PROJET EN MEME TEMPS QUE LA MAJ DE LA TACHE'
//         // this.subscribeToSaveResponse2(
//         //     this.tacheService.majJoursVendProj(this.tache));
//
//         // ci dessous, cree par moiSina
//         // if (this.tache.joursVendusTache != null)
//         // {
//         //     for(var i = 0;i<this.tache.joursVendusTache;i++) {
//         //         let attributionTache: AttributionTache;
//         //         this.subscribeToSaveResponse(
//         //             this.attributionTacheService.create(attributionTache));
//         //     }
//         // }
//     }




    // private subscribeToSaveResponse(result: Observable<Tache>) {
    //     result.subscribe((res: Tache) =>
    //         this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    // }

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

    // private onSaveSuccess(result: Tache) {
    //     this.eventManager.broadcast({ name: 'tacheListModification', content: 'OK'});
    //     this.isSaving = false;
    //     this.activeModal.dismiss(result);
    // }
    //
    // private onSaveError() {
    //     this.isSaving = false;
    // }
    //
    // private onError(error: any) {
    //     this.alertService.error(error.message, null, null);
    // }
    //
    // trackProjetById(index: number, item: Projet) {
    //     return item.id;
    // }




//    rjaoute par moiSina
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTaches() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tacheListModification',
            (response) => this.load(this.tache.id)
        );
    }

    registerChangeInNombreJours() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tacheListModification',
            (response) => this.load(this.tache.id)
        );
    }


    nombreDeJoursAttribuesMeth(idInge, idTache){
        this.attributionTacheService.nombreDeJoursAttribues(idInge, idTache).subscribe(
            (res: ResponseWrapper) => {
                this.nombreDeJoursAttribuesVar = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );

    }


    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    selectPuisUpdateNbJoursAffecter(idTache: number, idInge: number, nbJour: number) {

        console.log("µµµµµµ : " + idTache + " -- µµµµµµµ : " + idInge+ " - µµµµµµ : " + nbJour);
        if (nbJour%0.25 != 0 )
        {
            alert("Vous ne pouvez affecter qu'un nombre de jours entier, ou se terminant par 0.25 ou 0.75!");
            this.ngOnInit();
        }
        else if (nbJour > this.nombreMaxJours){
            alert("Vous ne pouvez pas affecter un nombre de jours supérieurs au nombre de jours restant à affecter pour cette tâche. Veuillez donc entrer un nombre de jours inférieurs ou égal à " + nbJour + " jours.");
            this.ngOnInit();
        }

       // voici la 1ere version que j'ai mis en place'
        //si je choissis cette verison , il me faut mettre dans le prototype de cette methode: editForm: NgForm
        //console.log("µµµµµµ" + idTache + "µµµµµµµ" + editForm.controls['idinge2'].value + "µµµµµµ" + editForm.controls['nombreDeJoursAaffecter'].value);

        // voici la methode que nous avons choisi avec Ayoub:

        else{
            this.attributionTacheService.getAttrTacheLimit(idTache, idInge, nbJour).subscribe(
                (res: ResponseWrapper) => {
                    this.listeAttribTaches = res.json;
                },
                (res: ResponseWrapper) => this.onError(res.json)
            );
            alert("Vous venez d'affecter " + nbJour + " jours.");
            this.ngOnInit();
        }





        //2eme methode
//        console.log("µµµµµµµµµµµµµµµµµµµµµµµµµµ" + idTache + "µµµµµµµµµµµµµµµµµµµµµµµµµµ" + editForm.value.idinge);

        //3eme methode:
        //console.log(editForm.value);

        // this.attributionTacheService.getAttrTacheLimit(idTache).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.listeAttribTache = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );

        //
        // this.isSaving = true;
        // if (this.attributionTache.id !== undefined) {
        //     this.subscribeToSaveResponse(
        //         this.attributionTacheService.update(this.attributionTache));
        // } else {
        //     this.subscribeToSaveResponse(
        //         this.attributionTacheService.create(this.attributionTache));
        // }
    }



    selectPuisUpdateNbJoursIngeSansAffec(idTache: number, loginInge: string, nbJour: number) {


        // voici la 1ere version que j'ai mis en place'
        //si je choissis cette verison , il me faut mettre dans le prototype de cette methode: editForm: NgForm
        //console.log("µµµµµµ" + idTache + "µµµµµµµ" + editForm.controls['idinge2'].value + "µµµµµµ" + editForm.controls['nombreDeJoursAaffecter'].value);

        // voici la methode que nous avons choisi avec Ayoub:
        console.log("µµµµµµ : " + idTache + " -- µµµµµµµ : " + loginInge+ " - µµµµµµ : " + nbJour);


        if (nbJour%0.25 != 0 )
        {
            alert("Vous ne pouvez affecter qu'un nombre de jours entier, ou se terminant par 0.25 ou 0.75!");
            this.ngOnInit();
        }
        else if (nbJour > this.nombreMaxJours){
            alert("Vous ne pouvez pas affecter un nombre de jours supérieurs au nombre de jours restant à affecter pour cette tâche. Veuillez donc entrer un nombre de jours inférieurs ou égal à " + nbJour + " jours.");
            this.ngOnInit();
        }
        else{

            // faire une methode qu_i recupere lid de linge a partir de son login

            this.employeService.getIdFromLogin(loginInge).subscribe(
                (res: ResponseWrapper) => {
                    this.idIngeSansAffectation = res.json;
                    // this.idIngeSansAffecTemp = this.idIngeSansAffectation;
                    console.log("a l'interieur de getIdFromLogin : " + this.idIngeSansAffectation);
                    console.log("a l'interieur de mon suybscrine : " + idTache + this.idIngeSansAffectation + nbJour);


                    // nous avons ici un subscribe, a linterireur dun autre subscribe
                    this.attributionTacheService.getAttrTacheLimit(idTache, this.idIngeSansAffectation, nbJour).subscribe(
                        (res: ResponseWrapper) => {
                            this.listeAttribTaches = res.json;
                        },
                        (res: ResponseWrapper) => this.onError(res.json)
                    );




                },
                (res: ResponseWrapper) => this.onError(res.json)
            );

            console.log("a l'exterieur de getIdFromLogin : " + this.idIngeSansAffecTemp);

            // this.attributionTacheService.getAttrTacheLimit(idTache, this.idIngeSansAffecTemp, nbJour).subscribe(
            //     (res: ResponseWrapper) => {
            //         this.listeAttribTaches = res.json;
            //     },
            //     (res: ResponseWrapper) => this.onError(res.json)
            // );
            alert("Vous venez d'affecter " + nbJour + " jours.");
            this.ngOnInit();
        }
    }


}




@Component({
    selector: 'jhi-nouvelle-affectation-tache-popup',
    // template: './nouvelle-affectation-dialog.component.html'
    template: ''
})
export class NouvelleAffectationTachePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tachePopupService: TachePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                console.log(" ludni matin params :: " + JSON.stringify(params));
                this.tachePopupService
                    .open(NouvelleAffectationDialogComponent as Component, params['id']);
            } else {
                this.tachePopupService
                    .open(NouvelleAffectationDialogComponent as Component);
            }
        });
    }




    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
