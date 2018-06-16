import { BaseEntity } from './../../shared';

export class Tache implements BaseEntity {
    constructor(
        public id?: number,
        public nomTache?: string,
        public descriptionTache?: string,
        public debutTache?: any,
        public finTache?: any,
        public nbQuartJourAttribuer?: number,
        public joursVendusTache?: number,
        public tacheEmployes?: BaseEntity[],
        public projetMere?: BaseEntity,
    ) {
    }
}



export class Tache2 implements BaseEntity {


    constructor(
        public projetMere: BaseEntity,
        public id?: number,
        public nomTache?: string,
        public descriptionTache?: string,
        public debutTache?: any,
        public finTache?: any,
        public nbQuartJourAttribuer?: number,
        public joursVendusTache?: number,
        public tacheEmployes?: BaseEntity[]
    ) {
        this.projetMere = projetMere;
        console.log("je suis dans le fichier model : " + this.projetMere);
    }
}
