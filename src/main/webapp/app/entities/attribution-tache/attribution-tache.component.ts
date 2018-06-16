import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { AttributionTache } from './attribution-tache.model';
import { AttributionTacheService } from './attribution-tache.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-attribution-tache',
    templateUrl: './attribution-tache.component.html'
})
export class AttributionTacheComponent implements OnInit, OnDestroy {
attributionTaches: AttributionTache[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private attributionTacheService: AttributionTacheService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.attributionTacheService.query().subscribe(
            (res: ResponseWrapper) => {
                this.attributionTaches = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInAttributionTaches();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: AttributionTache) {
        return item.id;
    }
    registerChangeInAttributionTaches() {
        this.eventSubscriber = this.eventManager.subscribe('attributionTacheListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
