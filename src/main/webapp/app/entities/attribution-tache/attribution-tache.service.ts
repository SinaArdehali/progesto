import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { AttributionTache } from './attribution-tache.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import {Tache} from "../tache/tache.model";

@Injectable()
export class AttributionTacheService {

    private resourceUrl = SERVER_API_URL + 'api/attribution-taches';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(attributionTache: AttributionTache): Observable<AttributionTache> {
        const copy = this.convert(attributionTache);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }


    update(attributionTache: AttributionTache): Observable<AttributionTache> {
        const copy = this.convert(attributionTache);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<AttributionTache> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.date = this.dateUtils
            .convertLocalDateFromServer(entity.date);
    }

    private convert(attributionTache: AttributionTache): AttributionTache {
        const copy: AttributionTache = Object.assign({}, attributionTache);
        copy.date = this.dateUtils
            .convertLocalDateToServer(attributionTache.date);
        return copy;
    }

    nombreDeJoursAttribues(idInge: number, idTache: number): Observable<ResponseWrapper> {
        // console.log(idInge, idTache);
        return this.http.get(`${SERVER_API_URL}api/attribution-taches/nbjoursattribues/${idInge}/${idTache}`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }

    affectationJoursInge(idInge: number): Observable<ResponseWrapper> {
        // console.log(idInge, idTache);
        return this.http.get(`${SERVER_API_URL}api/attribution-taches/affectationJoursInge/${idInge}`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }


    getAttrTacheLimit(idTache: number, idInge:number, nbJour: number): Observable<ResponseWrapper> {
        console.log("nbJour ------------------- : " + nbJour);
        return this.http.get(`${SERVER_API_URL}api/attribution-taches/getAttrTacheLimit/${idTache}/${idInge}/${nbJour}/`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }


    getMaxJours(idTache: number): Observable<ResponseWrapper> {
        return this.http.get(`${SERVER_API_URL}api/attribution-taches/getMaxJours/${idTache}`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }





}
