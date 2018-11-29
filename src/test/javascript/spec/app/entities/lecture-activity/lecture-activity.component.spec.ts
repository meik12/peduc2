/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Peduc2TestModule } from '../../../test.module';
import { LectureActivityComponent } from 'app/entities/lecture-activity/lecture-activity.component';
import { LectureActivityService } from 'app/entities/lecture-activity/lecture-activity.service';
import { LectureActivity } from 'app/shared/model/lecture-activity.model';

describe('Component Tests', () => {
    describe('LectureActivity Management Component', () => {
        let comp: LectureActivityComponent;
        let fixture: ComponentFixture<LectureActivityComponent>;
        let service: LectureActivityService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [LectureActivityComponent],
                providers: []
            })
                .overrideTemplate(LectureActivityComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(LectureActivityComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LectureActivityService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new LectureActivity(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.lectureActivities[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
