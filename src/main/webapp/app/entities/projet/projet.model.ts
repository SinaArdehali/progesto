import { BaseEntity, User } from './../../shared';

export class Projet implements BaseEntity {
    constructor(
        public id?: number,
        public nomProjet?: string,
        public descriptionProjet?: string,
        public debutProjet?: any,
        public finProjet?: any,
        public joursVendusProjet?: number,
        public tacheEnfants?: BaseEntity[],
        public membresProjets?: User[],
        public chefDuProjets?: User[],
        //public chefDuProjet?: User
        // public chefDuProjets?: User,
    ) {
    }
}
