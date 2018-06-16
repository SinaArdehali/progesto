import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Tache } from './tache.model';
import { TacheService } from './tache.service';

@Component({
    selector: 'jhi-tache-detail',
    templateUrl: './tache-detail.component.html'
})
export class TacheDetailComponent implements OnInit, OnDestroy {

    tache: Tache;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tacheService: TacheService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTaches();
    }

    load(id) {
        this.tacheService.find(id).subscribe((tache) => {
            this.tache = tache;
        });
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
}
