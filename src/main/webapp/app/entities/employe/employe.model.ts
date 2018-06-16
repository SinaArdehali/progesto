import { BaseEntity, User } from './../../shared';

export class Employe implements BaseEntity {
    constructor(
        public id?: number,
        public login?: string,
        public chefDeProjet?: boolean,
        public loginDansLeLdap?: User,
        public tachePersos?: BaseEntity[],
        public surQuelProjetEstIlInscrits?: BaseEntity[],
    ) {
        this.chefDeProjet = false;
    }
}
