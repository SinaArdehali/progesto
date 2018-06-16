import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { AttributionTache } from './attribution-tache.model';
import { AttributionTacheService } from './attribution-tache.service';

@Component({
    selector: 'jhi-attribution-tache-detail',
    templateUrl: './attribution-tache-detail.component.html'
})
export class AttributionTacheDetailComponent implements OnInit, OnDestroy {

    attributionTache: AttributionTache;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private attributionTacheService: AttributionTacheService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAttributionTaches();
    }

    load(id) {
        this.attributionTacheService.find(id).subscribe((attributionTache) => {
            this.attributionTache = attributionTache;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAttributionTaches() {
        this.eventSubscriber = this.eventManager.subscribe(
            'attributionTacheListModification',
            (response) => this.load(this.attributionTache.id)
        );
    }
}
