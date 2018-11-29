import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILectureActivity } from 'app/shared/model/lecture-activity.model';

type EntityResponseType = HttpResponse<ILectureActivity>;
type EntityArrayResponseType = HttpResponse<ILectureActivity[]>;

@Injectable({ providedIn: 'root' })
export class LectureActivityService {
    public resourceUrl = SERVER_API_URL + 'api/lecture-activities';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/lecture-activities';

    constructor(private http: HttpClient) {}

    create(lectureActivity: ILectureActivity): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(lectureActivity);
        return this.http
            .post<ILectureActivity>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(lectureActivity: ILectureActivity): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(lectureActivity);
        return this.http
            .put<ILectureActivity>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ILectureActivity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILectureActivity[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILectureActivity[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(lectureActivity: ILectureActivity): ILectureActivity {
        const copy: ILectureActivity = Object.assign({}, lectureActivity, {
            presentationDate:
                lectureActivity.presentationDate != null && lectureActivity.presentationDate.isValid()
                    ? lectureActivity.presentationDate.toJSON()
                    : null,
            postedDate:
                lectureActivity.postedDate != null && lectureActivity.postedDate.isValid() ? lectureActivity.postedDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.presentationDate = res.body.presentationDate != null ? moment(res.body.presentationDate) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((lectureActivity: ILectureActivity) => {
            lectureActivity.presentationDate = lectureActivity.presentationDate != null ? moment(lectureActivity.presentationDate) : null;
            lectureActivity.postedDate = lectureActivity.postedDate != null ? moment(lectureActivity.postedDate) : null;
        });
        return res;
    }
}
