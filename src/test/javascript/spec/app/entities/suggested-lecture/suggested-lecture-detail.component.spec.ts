/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { SuggestedLectureDetailComponent } from 'app/entities/suggested-lecture/suggested-lecture-detail.component';
import { SuggestedLecture } from 'app/shared/model/suggested-lecture.model';

describe('Component Tests', () => {
    describe('SuggestedLecture Management Detail Component', () => {
        let comp: SuggestedLectureDetailComponent;
        let fixture: ComponentFixture<SuggestedLectureDetailComponent>;
        const route = ({ data: of({ suggestedLecture: new SuggestedLecture(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [SuggestedLectureDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SuggestedLectureDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SuggestedLectureDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.suggestedLecture).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
