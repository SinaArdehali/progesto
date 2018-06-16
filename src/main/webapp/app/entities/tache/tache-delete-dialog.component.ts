import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Tache } from './tache.model';
import { TachePopupService } from './tache-popup.service';
import { TacheService } from './tache.service';

@Component({
    selector: 'jhi-tache-delete-dialog',
    templateUrl: './tache-delete-dialog.component.html'
})
export class TacheDeleteDialogComponent {

    tache: Tache;

    constructor(
        private tacheService: TacheService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tacheService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tacheListModification',
                content: 'Deleted an tache'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tache-delete-popup',
    template: ''
})
export class TacheDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tachePopupService: TachePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tachePopupService
                .open(TacheDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
