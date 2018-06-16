import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Projet } from './projet.model';
import { ProjetService } from './projet.service';

@Injectable()
export class ProjetPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private projetService: ProjetService

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
                this.projetService.find(id).subscribe((projet) => {
                    if (projet.debutProjet) {
                        projet.debutProjet = {
                            year: projet.debutProjet.getFullYear(),
                            month: projet.debutProjet.getMonth() + 1,
                            day: projet.debutProjet.getDate()
                        };
                    }
                    if (projet.finProjet) {
                        projet.finProjet = {
                            year: projet.finProjet.getFullYear(),
                            month: projet.finProjet.getMonth() + 1,
                            day: projet.finProjet.getDate()
                        };
                    }
                    this.ngbModalRef = this.projetModalRef(component, projet);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projetModalRef(component, new Projet());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    projetModalRef(component: Component, projet: Projet): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projet = projet;
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
