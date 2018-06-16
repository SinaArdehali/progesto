
import { Inject, Injectable } from '@angular/core';
// import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { ResponseWrapper, createRequestOption } from '../shared';
import {JhiDateUtils} from "ng-jhipster";
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../app.constants';








export type InternalStateType = {
    [key: string]: any
};



@Injectable()
export class ActiviteService {



    private resourceUrl = SERVER_API_URL + 'api/projets';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }


    public _state: InternalStateType = { };

    /**
     * Already return a clone of the current state.
     */
    public get state() {
        return this._state = this._clone(this._state);
    }
    /**
     * Never allow mutation
     */
    public set state(value) {
        throw new Error('do not mutate the `.state` directly');
    }

    public get(prop?: any) {
        /**
         * Use our state getter for the clone.
         */
        const state = this.state;
        return state.hasOwnProperty(prop) ? state[prop] : state;
    }

    public set(prop: string, value: any) {
        /**
         * Internally mutate our state.
         */
        return this._state[prop] = value;
    }

    private _clone(object: InternalStateType) {
        /**
         * Simple object clone.
         */
        return JSON.parse(JSON.stringify( object ));
    }

    getTachesCurrentUser(login: string): Observable<ResponseWrapper> {

        return this.http.get(`${SERVER_API_URL}api/taches/tachesCurrentUser/${login}`, {
        })
            .map((res: Response) => this.convertResponse(res));
    }


    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.debutProjet = this.dateUtils
            .convertLocalDateFromServer(entity.debutProjet);
        entity.finProjet = this.dateUtils
            .convertLocalDateFromServer(entity.finProjet);
    }


};
