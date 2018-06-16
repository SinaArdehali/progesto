/**
 * Created by sardehali on 01/02/18.
 */
import { BaseEntity } from './../../shared';
import {User} from "../../shared/user/user.model";
// import ParticTacheDTO from "../../............"

export class ParticTacheDTO implements BaseEntity {
    constructor(
        public id?: number,
        public user?: User,
        public count?: number
        // public id?: number;
        // public userLogin?: string;
        // public count?: number;

    ) {
    }
}
