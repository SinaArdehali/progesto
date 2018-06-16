import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Projet } from './projet.model';
import { ProjetService } from './projet.service';

@Component({
    selector: 'jhi-projet-detail',
    templateUrl: './projet-detail.component.html'
})
export class ProjetDetailComponent implements OnInit, OnDestroy {

    projet: Projet;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private projetService: ProjetService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjets();
    }

    load(id) {
        this.projetService.find(id).subscribe((projet) => {
            this.projet = projet;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjets() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projetListModification',
            (response) => this.load(this.projet.id)
        );
    }
}
