import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AttributionTache } from './attribution-tache.model';
import { AttributionTachePopupService } from './attribution-tache-popup.service';
import { AttributionTacheService } from './attribution-tache.service';
import { User, UserService } from '../../shared';
import { Tache, TacheService } from '../tache';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-attribution-tache-dialog',
    templateUrl: './attribution-tache-dialog.component.html'
})
export class AttributionTacheDialogComponent implements OnInit {

    attributionTache: AttributionTache;
    isSaving: boolean;

    users: User[];

    taches: Tache[];
    dateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private attributionTacheService: AttributionTacheService,
        private userService: UserService,
        private tacheService: TacheService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.tacheService.query()
            .subscribe((res: ResponseWrapper) => { this.taches = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.attributionTache.id !== undefined) {
            this.subscribeToSaveResponse(
                this.attributionTacheService.update(this.attributionTache));
        } else {
            this.subscribeToSaveResponse(
                this.attributionTacheService.create(this.attributionTache));
        }
    }

    private subscribeToSaveResponse(result: Observable<AttributionTache>) {
        result.subscribe((res: AttributionTache) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AttributionTache) {
        this.eventManager.broadcast({ name: 'attributionTacheListModification', content: 'OK'});
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

    trackTacheById(index: number, item: Tache) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-attribution-tache-popup',
    template: ''
})
export class AttributionTachePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attributionTachePopupService: AttributionTachePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.attributionTachePopupService
                    .open(AttributionTacheDialogComponent as Component, params['id']);
            } else {
                this.attributionTachePopupService
                    .open(AttributionTacheDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
