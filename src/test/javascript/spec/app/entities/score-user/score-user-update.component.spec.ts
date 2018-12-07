/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { ScoreUserUpdateComponent } from 'app/entities/score-user/score-user-update.component';
import { ScoreUserService } from 'app/entities/score-user/score-user.service';
import { ScoreUser } from 'app/shared/model/score-user.model';

describe('Component Tests', () => {
    describe('ScoreUser Management Update Component', () => {
        let comp: ScoreUserUpdateComponent;
        let fixture: ComponentFixture<ScoreUserUpdateComponent>;
        let service: ScoreUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [ScoreUserUpdateComponent]
            })
                .overrideTemplate(ScoreUserUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ScoreUserUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScoreUserService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ScoreUser(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.scoreUser = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ScoreUser();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.scoreUser = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
