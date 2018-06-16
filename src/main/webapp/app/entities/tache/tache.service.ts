import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Tache } from './tache.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import {Projet} from "../projet/projet.model";

@Injectable()
export class TacheService {

    private resourceUrl = SERVER_API_URL + 'api/taches';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(tache: Tache): Observable<Tache> {
        const copy = this.convert(tache);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(tache: Tache): Observable<Tache> {
        const copy = this.convert(tache);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }


    find(id: number): Observable<Tache> {
        console.log("on est dans le find de tache.service.ts");
        console.log(id);

        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            console.log("=======================" + jsonResponse.toString());
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
        entity.debutTache = this.dateUtils
            .convertLocalDateFromServer(entity.debutTache);
        entity.finTache = this.dateUtils
            .convertLocalDateFromServer(entity.finTache);
    }

    private convert(tache: Tache): Tache {
        const copy: Tache = Object.assign({}, tache);
        copy.debutTache = this.dateUtils
            .convertLocalDateToServer(tache.debutTache);
        copy.finTache = this.dateUtils
            .convertLocalDateToServer(tache.finTache);
        return copy;
    }



// JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR A PRESENT LA MAJ DU PROJET EN MEME TEMPS QUE LA MAJ DE LA TACHE'
//     majJoursVendProj(tache: Tache): Observable<Projet> {
//         // console.log(JSON.stringify(tache));
//         const copy = this.convert(tache);
//         return this.http.put(`${SERVER_API_URL}api/taches/projet/`, copy).map((res: Response) => {
//             const jsonResponse = res.json();
//             this.convertItemFromServer(jsonResponse);
//             return jsonResponse;
//         });
//     }




// en balancant la tache en entier:
//     modifierJoursVendus(nbJour: number, idTacheMere: number): Observable<ResponseWrapper> {
//         console.log("dans modifierJoursVendus : " + nbJour + idTacheMere);
//         return this.http.get(`${SERVER_API_URL}api/taches/modifierJoursVendus/${nbJour}/${idTacheMere}/`, {
//
//         })
//             .map((res: Response) => this.convertResponse(res));
//     }


    // en balancant la tache en entier:
    ajouterJoursVendus(nbJour: number, idTacheMere: number): Observable<ResponseWrapper> {
        console.log("dans ajouterJoursVendus : " + nbJour + idTacheMere);
        return this.http.get(`${SERVER_API_URL}api/taches/modifierJoursVendus/${nbJour}/${idTacheMere}/`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }

    selectPuisRetirerJoursVendus(nbJour: number, idTacheMere: number): Observable<ResponseWrapper> {
        console.log("dans modifierJoursVendus : " + nbJour + idTacheMere);
        return this.http.get(`${SERVER_API_URL}api/taches/selectPuisRetirerJoursVendus/${nbJour}/${idTacheMere}/`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }


}
