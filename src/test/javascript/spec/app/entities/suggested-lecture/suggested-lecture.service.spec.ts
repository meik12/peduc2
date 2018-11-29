/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SuggestedLectureService } from 'app/entities/suggested-lecture/suggested-lecture.service';
import { ISuggestedLecture, SuggestedLecture } from 'app/shared/model/suggested-lecture.model';

describe('Service Tests', () => {
    describe('SuggestedLecture Service', () => {
        let injector: TestBed;
        let service: SuggestedLectureService;
        let httpMock: HttpTestingController;
        let elemDefault: ISuggestedLecture;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SuggestedLectureService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new SuggestedLecture(
                0,
                'image/png',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                0,
                'AAAAAAA',
                'AAAAAAA',
                0,
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                'AAAAAAA',
                currentDate
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        presentationDate: currentDate.format(DATE_TIME_FORMAT),
                        publicationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a SuggestedLecture', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        presentationDate: currentDate.format(DATE_TIME_FORMAT),
                        publicationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        presentationDate: currentDate,
                        publicationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new SuggestedLecture(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a SuggestedLecture', async () => {
                const returnedFromService = Object.assign(
                    {
                        profilePicture: 'BBBBBB',
                        firstName: 'BBBBBB',
                        lastName: 'BBBBBB',
                        averageScore: 1,
                        category: 'BBBBBB',
                        title: 'BBBBBB',
                        duration: 1,
                        language: 'BBBBBB',
                        videoCallLink: 'BBBBBB',
                        presentationDate: currentDate.format(DATE_TIME_FORMAT),
                        timeZone: 'BBBBBB',
                        publicationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        presentationDate: currentDate,
                        publicationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of SuggestedLecture', async () => {
                const returnedFromService = Object.assign(
                    {
                        profilePicture: 'BBBBBB',
                        firstName: 'BBBBBB',
                        lastName: 'BBBBBB',
                        averageScore: 1,
                        category: 'BBBBBB',
                        title: 'BBBBBB',
                        duration: 1,
                        language: 'BBBBBB',
                        videoCallLink: 'BBBBBB',
                        presentationDate: currentDate.format(DATE_TIME_FORMAT),
                        timeZone: 'BBBBBB',
                        publicationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        presentationDate: currentDate,
                        publicationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(take(1), map(resp => resp.body))
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a SuggestedLecture', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
