/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Peduc2TestModule } from '../../../test.module';
import { ScoreUserComponent } from 'app/entities/score-user/score-user.component';
import { ScoreUserService } from 'app/entities/score-user/score-user.service';
import { ScoreUser } from 'app/shared/model/score-user.model';

describe('Component Tests', () => {
    describe('ScoreUser Management Component', () => {
        let comp: ScoreUserComponent;
        let fixture: ComponentFixture<ScoreUserComponent>;
        let service: ScoreUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [ScoreUserComponent],
                providers: []
            })
                .overrideTemplate(ScoreUserComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ScoreUserComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScoreUserService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ScoreUser(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.scoreUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
