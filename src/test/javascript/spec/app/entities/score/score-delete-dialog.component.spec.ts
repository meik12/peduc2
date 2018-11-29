/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Peduc2TestModule } from '../../../test.module';
import { ScoreDeleteDialogComponent } from 'app/entities/score/score-delete-dialog.component';
import { ScoreService } from 'app/entities/score/score.service';

describe('Component Tests', () => {
    describe('Score Management Delete Component', () => {
        let comp: ScoreDeleteDialogComponent;
        let fixture: ComponentFixture<ScoreDeleteDialogComponent>;
        let service: ScoreService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [ScoreDeleteDialogComponent]
            })
                .overrideTemplate(ScoreDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ScoreDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScoreService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
