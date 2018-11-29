/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { LectureActivityDetailComponent } from 'app/entities/lecture-activity/lecture-activity-detail.component';
import { LectureActivity } from 'app/shared/model/lecture-activity.model';

describe('Component Tests', () => {
    describe('LectureActivity Management Detail Component', () => {
        let comp: LectureActivityDetailComponent;
        let fixture: ComponentFixture<LectureActivityDetailComponent>;
        const route = ({ data: of({ lectureActivity: new LectureActivity(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [LectureActivityDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(LectureActivityDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LectureActivityDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.lectureActivity).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
