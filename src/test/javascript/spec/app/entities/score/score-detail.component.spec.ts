/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { ScoreDetailComponent } from 'app/entities/score/score-detail.component';
import { Score } from 'app/shared/model/score.model';

describe('Component Tests', () => {
    describe('Score Management Detail Component', () => {
        let comp: ScoreDetailComponent;
        let fixture: ComponentFixture<ScoreDetailComponent>;
        const route = ({ data: of({ score: new Score(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [ScoreDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ScoreDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ScoreDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.score).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
