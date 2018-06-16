import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Employe } from './employe.model';
import { EmployePopupService } from './employe-popup.service';
import { EmployeService } from './employe.service';

@Component({
    selector: 'jhi-employe-delete-dialog',
    templateUrl: './employe-delete-dialog.component.html'
})
export class EmployeDeleteDialogComponent {

    employe: Employe;

    constructor(
        private employeService: EmployeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.employeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'employeListModification',
                content: 'Deleted an employe'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-employe-delete-popup',
    template: ''
})
export class EmployeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employePopupService: EmployePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.employePopupService
                .open(EmployeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
