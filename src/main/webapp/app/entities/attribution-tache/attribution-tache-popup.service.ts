import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AttributionTache } from './attribution-tache.model';
import { AttributionTacheService } from './attribution-tache.service';

@Injectable()
export class AttributionTachePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private attributionTacheService: AttributionTacheService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.attributionTacheService.find(id).subscribe((attributionTache) => {
                    if (attributionTache.date) {
                        attributionTache.date = {
                            year: attributionTache.date.getFullYear(),
                            month: attributionTache.date.getMonth() + 1,
                            day: attributionTache.date.getDate()
                        };
                    }
                    this.ngbModalRef = this.attributionTacheModalRef(component, attributionTache);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.attributionTacheModalRef(component, new AttributionTache());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    attributionTacheModalRef(component: Component, attributionTache: AttributionTache): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.attributionTache = attributionTache;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
