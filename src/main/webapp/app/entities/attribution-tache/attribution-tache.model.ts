import { BaseEntity, User } from './../../shared';

export class AttributionTache implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public quartJournee?: number,
        public proprietaireTache?: User,
        public tacheMere?: BaseEntity,
    ) {
    }
}
