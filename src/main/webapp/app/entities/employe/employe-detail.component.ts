import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Employe } from './employe.model';
import { EmployeService } from './employe.service';

@Component({
    selector: 'jhi-employe-detail',
    templateUrl: './employe-detail.component.html'
})
export class EmployeDetailComponent implements OnInit, OnDestroy {

    employe: Employe;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private employeService: EmployeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmployes();
    }

    load(id) {
        this.employeService.find(id).subscribe((employe) => {
            this.employe = employe;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmployes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'employeListModification',
            (response) => this.load(this.employe.id)
        );
    }
}
