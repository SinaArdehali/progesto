import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Employe } from './employe.model';
import { EmployeService } from './employe.service';
import { Principal, ResponseWrapper } from '../../shared';
import {User} from "../../shared/user/user.model";
import {Tache} from "../tache/tache.model";
import {Projet} from "../projet/projet.model";
import {TacheService} from "../tache/tache.service";
import {ProjetService} from "../projet/projet.service";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../shared/user/user.service";
import {AttributionTacheService} from "../attribution-tache/attribution-tache.service";

@Component({
    selector: 'jhi-employe',
    templateUrl: './employe.component.html',
    styleUrls: [
        './employe.component.css'
    ]
})
export class EmployeComponent implements OnInit, OnDestroy {
employes: User[];
    employesAvecMs: User[];
    currentAccount: any;
    eventSubscriber: Subscription;
    //ingenieursParTache: User[];
    participants: User[];
    personnesNonAffecteess: User[];
    //participants2:ParticTacheDTO[];
    nombreDeJoursAttribuesVar: number;
    private subscription: Subscription;
//    donc subscription correspond à  l'id que j'ai recupere dans le route

    tache: Tache;
    isSaving: boolean;

    projets: Projet[];
    debutTacheDp: any;
    finTacheDp: any;
    idValue: any;

    // je ne devrais pas le mettre dans un tableau de User
    getAllIngeNoAttribs: String[];


    constructor(
        private employeService: EmployeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private alertService: JhiAlertService,
        private tacheService: TacheService,
        private projetService: ProjetService,
        private route: ActivatedRoute,
        private userService: UserService,
        private attributionTacheService: AttributionTacheService
    ) {
    }

    loadAll() {
        //ci dessous j'ai recupere tous les ingenieurs sans les valeurs de m, m+1, m+2'
        this.employeService.getAllInge().subscribe(
            (res: ResponseWrapper) => {
                this.employes = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );

        //ci dessous ,j'ai recupere tous les ingenieurs avec les valeurs de m, m+1, m+2'
        // this.userService.getParticByTache(id).subscribe(
        //     (res: ResponseWrapper) => {
        //         this.participants = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );
        this.employeService.getAllIngeM().subscribe(
            (res: ResponseWrapper) => {
                this.employesAvecMs = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );


        this.employeService.getAllIngeNoAttrib().subscribe(
            (res: ResponseWrapper) => {
                this.getAllIngeNoAttribs = res.json;
                console.log("this.getAllIngeNoAttribs.toString() : "+ JSON.stringify(this.getAllIngeNoAttribs));
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );



    }
    ngOnInit() {

        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
       this.registerChangeInEmployes();

        // console.log("JSON.stringify(this.tache)) : " + JSON.stringify(this.tache));
        // this.idValue = this.tache.id;
        // console.log("voici l'id de la tache " + this.idValue);

        // this.subscription = this.route.params.subscribe((params) => {
        //     //console.log(" params['id'] :: " + params['id']);
        //     // console.log("params :: " + JSON.stringify(params));
        //     console.log("on est ici dans le component du haut NouvelleAffectationDialogComponent" + JSON.stringify(params));
        //     // console.log(" ludni matin params :: " + JSON.stringify(params));
        //     this.load(this.idValue);
        // });
    }

//     load(id) {
//         // console.log("dans le load");
//         // a ce niveau, id est undefined
// // console.log(id);
//         this.tacheService.find(id);
//         // this.tacheService.find(id).subscribe((tache) => {
//         //     this.tache = tache;
//         //     console.log("dans mon load de nouvelle tache affectation");
//         //     console.log(this.tache);
//         // });
//
//         this.userService.queryPersonnesNonAffectees().subscribe(
//             (res: ResponseWrapper) => {
//                 this.personnesNonAffecteess = res.json;
//             },
//             (res: ResponseWrapper) => this.onError(res.json)
//         );
//
//         // this.projetService.query().subscribe(
//         //     (res: ResponseWrapper) => {
//         //         this.projets = res.json;
//         //     },
//         //     (res: ResponseWrapper) => this.onError(res.json)
//         // );
//     }

    ngOnDestroy() {
        //this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Employe) {
        return item.id;
    }

    registerChangeInEmployes() {
        this.eventSubscriber = this.eventManager.subscribe('employeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
