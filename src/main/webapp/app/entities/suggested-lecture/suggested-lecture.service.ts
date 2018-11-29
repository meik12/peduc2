import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISuggestedLecture } from 'app/shared/model/suggested-lecture.model';

type EntityResponseType = HttpResponse<ISuggestedLecture>;
type EntityArrayResponseType = HttpResponse<ISuggestedLecture[]>;

@Injectable({ providedIn: 'root' })
export class SuggestedLectureService {
    public resourceUrl = SERVER_API_URL + 'api/suggested-lectures';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/suggested-lectures';

    constructor(private http: HttpClient) {}

    create(suggestedLecture: ISuggestedLecture): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(suggestedLecture);
        return this.http
            .post<ISuggestedLecture>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(suggestedLecture: ISuggestedLecture): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(suggestedLecture);
        return this.http
            .put<ISuggestedLecture>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISuggestedLecture>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISuggestedLecture[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISuggestedLecture[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(suggestedLecture: ISuggestedLecture): ISuggestedLecture {
        const copy: ISuggestedLecture = Object.assign({}, suggestedLecture, {
            presentationDate:
                suggestedLecture.presentationDate != null && suggestedLecture.presentationDate.isValid()
                    ? suggestedLecture.presentationDate.toJSON()
                    : null,
            publicationDate:
                suggestedLecture.publicationDate != null && suggestedLecture.publicationDate.isValid()
                    ? suggestedLecture.publicationDate.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.presentationDate = res.body.presentationDate != null ? moment(res.body.presentationDate) : null;
        res.body.publicationDate = res.body.publicationDate != null ? moment(res.body.publicationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((suggestedLecture: ISuggestedLecture) => {
            suggestedLecture.presentationDate =
                suggestedLecture.presentationDate != null ? moment(suggestedLecture.presentationDate) : null;
            suggestedLecture.publicationDate = suggestedLecture.publicationDate != null ? moment(suggestedLecture.publicationDate) : null;
        });
        return res;
    }
}
