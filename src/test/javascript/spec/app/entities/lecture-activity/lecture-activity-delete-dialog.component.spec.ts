/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Peduc2TestModule } from '../../../test.module';
import { LectureActivityDeleteDialogComponent } from 'app/entities/lecture-activity/lecture-activity-delete-dialog.component';
import { LectureActivityService } from 'app/entities/lecture-activity/lecture-activity.service';

describe('Component Tests', () => {
    describe('LectureActivity Management Delete Component', () => {
        let comp: LectureActivityDeleteDialogComponent;
        let fixture: ComponentFixture<LectureActivityDeleteDialogComponent>;
        let service: LectureActivityService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [LectureActivityDeleteDialogComponent]
            })
                .overrideTemplate(LectureActivityDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LectureActivityDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LectureActivityService);
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
