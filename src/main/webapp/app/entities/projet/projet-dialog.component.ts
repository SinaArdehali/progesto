import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Projet } from './projet.model';
import { ProjetPopupService } from './projet-popup.service';
import { ProjetService } from './projet.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-projet-dialog',
    templateUrl: './projet-dialog.component.html',
    styleUrls: [
        './projet-dialog.component.css'
    ]
})
export class ProjetDialogComponent implements OnInit {

    projet: Projet;
    isSaving: boolean;

    users: User[];
    debutProjetDp: any;
    finProjetDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private projetService: ProjetService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.projet.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projetService.update(this.projet));
        } else {
            this.subscribeToSaveResponse(
                this.projetService.create(this.projet));
        }
    }

    private subscribeToSaveResponse(result: Observable<Projet>) {
        result.subscribe((res: Projet) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Projet) {
        this.eventManager.broadcast({ name: 'projetListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-projet-popup',
    template: ''
})
export class ProjetPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projetPopupService: ProjetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projetPopupService
                    .open(ProjetDialogComponent as Component, params['id']);
            } else {
                this.projetPopupService
                    .open(ProjetDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
