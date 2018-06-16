import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Employe } from './employe.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmployeService {

    private resourceUrl =  SERVER_API_URL + 'api/employes';
    private resourceUrl2 =  SERVER_API_URL + 'api/users';

    constructor(private http: Http) { }

    create(employe: Employe): Observable<Employe> {
        const copy = this.convert(employe);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(employe: Employe): Observable<Employe> {
        const copy = this.convert(employe);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Employe> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
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
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Employe.
     */
    private convertItemFromServer(json: any): Employe {
        const entity: Employe = Object.assign(new Employe(), json);
        return entity;
    }

    /**
     * Convert a Employe to a JSON which can be sent to the server.
     */
    private convert(employe: Employe): Employe {
        const copy: Employe = Object.assign({}, employe);
        return copy;
    }

//    cree par moiSina
    getAllInge(): Observable<ResponseWrapper> {
        return this.http.get(`${SERVER_API_URL}api/users/listeIngenieurs/`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }

    //rajoute par moiSina:
    getAllIngeM(): Observable<ResponseWrapper> {
        return this.http.get(`${SERVER_API_URL}api/users/getAllIngeM/`, {

        })
            .map((res: Response) => this.convertResponse(res));
    }

    //rajoute par moiSina:
    getAllIngeNoAttrib(): Observable<ResponseWrapper> {
        return this.http.get(`${SERVER_API_URL}api/users/getAllIngeNoAttrib/`, {

        })
            .map((res: Response) => {
                //const test = res.json();
                //console.log("test ---- :" + JSON.stringify(test[0]));
                //return this.convertResponse(res);

                // le bout de code cid essous est inspire de la methode convertResponse, mais dans cette methode , ce que l'on entre est converti en objet Employe. ci dessous , jai retire cette conversion'
                const jsonResponse = res.json();
                const result = [];
                for (let i = 0; i < jsonResponse.length; i++) {
                    result.push(jsonResponse[i]);
                }
                return new ResponseWrapper(res.headers, result, res.status);
            });
    }


    getIdFromLogin(login: string): Observable<ResponseWrapper> {
        return this.http.get(`${SERVER_API_URL}api/users/getIdFromLogin/${login}`, {

        })
            .map((res: Response) => {
                //const test = res.json();
                //console.log("test ---- :" + JSON.stringify(test[0]));
                //return this.convertResponse(res);

                // le bout de code cid essous est inspire de la methode convertResponse, mais dans cette methode , ce que l'on entre est converti en objet Employe. ci dessous , jai retire cette conversion'
                console.log(" display de login " + login);
                const jsonResponse = res.json();
                console.log("voici la valeur de mon res : " + JSON.stringify(res));
                const result = res.json();
                console.log("voici la valeur de mon result : " +result);
                // for (let i = 0; i < jsonResponse.length; i++) {
                //     result.push(jsonResponse[i]);
                // }
                return new ResponseWrapper(res.headers, result, res.status);
            });
    }


}

// select count(*) from attribution_tache
// where proprietaire_tache_id = 11452
// and jhi_date >= current_date
// and jhi_date <= current_date + interval '1 month' * 1;

