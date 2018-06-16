/**
 * Created by sardehali on 22/01/18.
 */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';


import { Tache } from './tache.model';
import { TacheService } from './tache.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {User} from "../../shared/user/user.model";
import {UserService} from "../../shared/user/user.service";
import {AttributionTacheService} from "../attribution-tache/attribution-tache.service";
//import {ParticTacheDTO} from "./ParticTacheDTO.model";


@Component({
    selector: 'jhi-tache-affectation',
    templateUrl: './tache-affectation.component.html',
    styleUrls: [
        './tache-affectation.component.css'
    ]
})
export class TacheAffectationComponent implements OnInit, OnDestroy {

    tache: Tache;
    //ingenieursParTache: User[];

    //de maniere tres etrange, nous avons stocké les objets  ParticTacheDTO dans des users
    participants: User[];

    //participants2:ParticTacheDTO[];
    nombreDeJoursAttribuesVar: number;
    private subscription: Subscription;
//    donc subscription correspond à  l'id que j'ai recupere dans le route
    currentAccount: any;
    private eventSubscriber: Subscription;



    constructor(
        private eventManager: JhiEventManager,
        private principal: Principal,
        private alertService: JhiAlertService,
        private tacheService: TacheService,
        private route: ActivatedRoute,
        private userService: UserService,
        private attributionTacheService: AttributionTacheService
    ) {
    }


    // loadAll(){
    //     this.userService.getParticByTache().subscribe(
    //         (res: ResponseWrapper) => {
    //             this.participants = res.json;
    //         },
    //         (res: ResponseWrapper) => this.onError(res.json)
    //     );
    //
    //
    // }


    ngOnInit() {
        // this.loadAll();
        //this.nombreDeJoursAttribuesMeth(11452,2101);

        // console.log("dans tache affectation");
        // console.log(this.route.params);
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
            console.log("ci dessous, le param. contient il l'id?")
            console.log(this.route.params);
        });
        // console.log(this.route.params);
        // this.principal.identity().then((account) => {
        //     this.currentAccount = account;
        // });

        this.registerChangeInTaches();
    }

    load(id) {
        this.tacheService.find(id).subscribe((tache) => {
            this.tache = tache;
            // console.log("dans mon load de tache affectation");
            // console.log(this.tache);
        });

        this.userService.getParticByTache(id).subscribe(
            (res: ResponseWrapper) => {
                this.participants = res.json;
                // console.log(" voici le contenu de this.participants.toString() : " + this.participants.toString());
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );

        // this.projetService.query().subscribe(
        //     (res: ResponseWrapper) => {
        //         this.projets = res.json;
        //     },
        //     (res: ResponseWrapper) => this.onError(res.json)
        // );
    }

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
}
