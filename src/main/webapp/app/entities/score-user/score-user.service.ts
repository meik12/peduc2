import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IScoreUser } from 'app/shared/model/score-user.model';

type EntityResponseType = HttpResponse<IScoreUser>;
type EntityArrayResponseType = HttpResponse<IScoreUser[]>;

@Injectable({ providedIn: 'root' })
export class ScoreUserService {
    public resourceUrl = SERVER_API_URL + 'api/score-users';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/score-users';

    constructor(private http: HttpClient) {}

    create(scoreUser: IScoreUser): Observable<EntityResponseType> {
        return this.http.post<IScoreUser>(this.resourceUrl, scoreUser, { observe: 'response' });
    }

    update(scoreUser: IScoreUser): Observable<EntityResponseType> {
        return this.http.put<IScoreUser>(this.resourceUrl, scoreUser, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IScoreUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IScoreUser[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IScoreUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
