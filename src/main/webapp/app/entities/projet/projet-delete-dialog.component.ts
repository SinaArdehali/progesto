import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Projet } from './projet.model';
import { ProjetPopupService } from './projet-popup.service';
import { ProjetService } from './projet.service';

@Component({
    selector: 'jhi-projet-delete-dialog',
    templateUrl: './projet-delete-dialog.component.html'
})
export class ProjetDeleteDialogComponent {

    projet: Projet;

    constructor(
        private projetService: ProjetService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projetService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projetListModification',
                content: 'Deleted an projet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-projet-delete-popup',
    template: ''
})
export class ProjetDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projetPopupService: ProjetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projetPopupService
                .open(ProjetDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
