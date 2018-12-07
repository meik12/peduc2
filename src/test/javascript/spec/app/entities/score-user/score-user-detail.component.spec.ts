/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { ScoreUserDetailComponent } from 'app/entities/score-user/score-user-detail.component';
import { ScoreUser } from 'app/shared/model/score-user.model';

describe('Component Tests', () => {
    describe('ScoreUser Management Detail Component', () => {
        let comp: ScoreUserDetailComponent;
        let fixture: ComponentFixture<ScoreUserDetailComponent>;
        const route = ({ data: of({ scoreUser: new ScoreUser(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [ScoreUserDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ScoreUserDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ScoreUserDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.scoreUser).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
