import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AttributionTache } from './attribution-tache.model';
import { AttributionTachePopupService } from './attribution-tache-popup.service';
import { AttributionTacheService } from './attribution-tache.service';

@Component({
    selector: 'jhi-attribution-tache-delete-dialog',
    templateUrl: './attribution-tache-delete-dialog.component.html'
})
export class AttributionTacheDeleteDialogComponent {

    attributionTache: AttributionTache;

    constructor(
        private attributionTacheService: AttributionTacheService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.attributionTacheService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'attributionTacheListModification',
                content: 'Deleted an attributionTache'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-attribution-tache-delete-popup',
    template: ''
})
export class AttributionTacheDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attributionTachePopupService: AttributionTachePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.attributionTachePopupService
                .open(AttributionTacheDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
