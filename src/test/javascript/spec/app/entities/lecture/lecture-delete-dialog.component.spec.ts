/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Peduc2TestModule } from '../../../test.module';
import { LectureDeleteDialogComponent } from 'app/entities/lecture/lecture-delete-dialog.component';
import { LectureService } from 'app/entities/lecture/lecture.service';

describe('Component Tests', () => {
    describe('Lecture Management Delete Component', () => {
        let comp: LectureDeleteDialogComponent;
        let fixture: ComponentFixture<LectureDeleteDialogComponent>;
        let service: LectureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [LectureDeleteDialogComponent]
            })
                .overrideTemplate(LectureDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LectureDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LectureService);
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
