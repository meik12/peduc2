import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILecture } from 'app/shared/model/lecture.model';

type EntityResponseType = HttpResponse<ILecture>;
type EntityArrayResponseType = HttpResponse<ILecture[]>;

@Injectable({ providedIn: 'root' })
export class LectureService {
    public resourceUrl = SERVER_API_URL + 'api/lectures';
    public resourceUrlForCurrent = SERVER_API_URL + 'api/lecturesForCurrent';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/lectures';
    public resourceUrlForPast = SERVER_API_URL + 'api/lecturesPastPresentation';

    constructor(private http: HttpClient) {}

    create(lecture: ILecture): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(lecture);
        return this.http
            .post<ILecture>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(lecture: ILecture): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(lecture);
        return this.http
            .put<ILecture>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ILecture>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILecture[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryForCurrent(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILecture[]>(this.resourceUrlForCurrent, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryForPastPresentation(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILecture[]>(this.resourceUrlForPast, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILecture[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(lecture: ILecture): ILecture {
        const copy: ILecture = Object.assign({}, lecture, {
            presentationDate:
                lecture.presentationDate != null && lecture.presentationDate.isValid() ? lecture.presentationDate.toJSON() : null,
            publicationDate: lecture.publicationDate != null && lecture.publicationDate.isValid() ? lecture.publicationDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.presentationDate = res.body.presentationDate != null ? moment(res.body.presentationDate) : null;
        res.body.publicationDate = res.body.publicationDate != null ? moment(res.body.publicationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((lecture: ILecture) => {
            lecture.presentationDate = lecture.presentationDate != null ? moment(lecture.presentationDate) : null;
            lecture.publicationDate = lecture.publicationDate != null ? moment(lecture.publicationDate) : null;
        });
        return res;
    }
}
