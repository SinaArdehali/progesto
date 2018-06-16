import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {Tache, Tache2} from './tache.model';
import { TacheService } from './tache.service';
import {ProjetService} from "../projet/projet.service";

@Injectable()
export class TachePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tacheService: TacheService,
        private projetService: ProjetService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any, idProjetMere?: number | any): Promise<NgbModalRef> {
        console.log("on est a louverture du open : "+ id);


        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.tacheService.find(id).subscribe((tache) => {
                    if (tache.debutTache) {
                        tache.debutTache = {
                            year: tache.debutTache.getFullYear(),
                            month: tache.debutTache.getMonth() + 1,
                            day: tache.debutTache.getDate()
                        };
                    }
                    if (tache.finTache) {
                        tache.finTache = {
                            year: tache.finTache.getFullYear(),
                            month: tache.finTache.getMonth() + 1,
                            day: tache.finTache.getDate()
                        };
                    }
                    console.log("je suis apres le bloc if de tachepopUpService" + tache.toString());
                    console.log(JSON.stringify(tache));
                    this.ngbModalRef = this.tacheModalRef(component, tache);
                    // console.log(this.ngbModalRef);
                    resolve(this.ngbModalRef);
                });
            }
            else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tacheModalRef(component, new Tache());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }


    open2(component: Component, idProjetMere?: number | any): Promise<NgbModalRef> {
        console.log("on est a louverture du open2 : "+ idProjetMere);


        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            this.projetService.find(idProjetMere).subscribe((projetMere) => {

                console.log("je suis dans le bloc else if de tachepopUpService de open2" + projetMere.toString());
                console.log(JSON.stringify(projetMere));
                this.ngbModalRef = this.tacheModalRef(component, new Tache2(projetMere));
                // console.log(this.ngbModalRef);
                resolve(this.ngbModalRef);

                console.log(" voici mon console log de mon this.ngbModalRef : " + this.ngbModalRef);
            });










            //
            //     console.log("je suis dans le bloc else if de tachepopUpService de open2" + idProjetMere);
            //     setTimeout(() => {
            //         this.ngbModalRef = this.tacheModalRef(component, new Tache2(idProjetMere));
            //         resolve(this.ngbModalRef);
            //     }, 0);
            // console.log(" voici mon console log de mon this.ngbModalRef : " + this.ngbModalRef);

        });
    }





    tacheModalRef(component: Component, tache: Tache): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tache = tache;
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
