/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { LectureDetailComponent } from 'app/entities/lecture/lecture-detail.component';
import { Lecture } from 'app/shared/model/lecture.model';

describe('Component Tests', () => {
    describe('Lecture Management Detail Component', () => {
        let comp: LectureDetailComponent;
        let fixture: ComponentFixture<LectureDetailComponent>;
        const route = ({ data: of({ lecture: new Lecture(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [LectureDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(LectureDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LectureDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.lecture).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
