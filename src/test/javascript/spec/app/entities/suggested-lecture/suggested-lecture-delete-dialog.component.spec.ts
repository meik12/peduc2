/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Peduc2TestModule } from '../../../test.module';
import { SuggestedLectureDeleteDialogComponent } from 'app/entities/suggested-lecture/suggested-lecture-delete-dialog.component';
import { SuggestedLectureService } from 'app/entities/suggested-lecture/suggested-lecture.service';

describe('Component Tests', () => {
    describe('SuggestedLecture Management Delete Component', () => {
        let comp: SuggestedLectureDeleteDialogComponent;
        let fixture: ComponentFixture<SuggestedLectureDeleteDialogComponent>;
        let service: SuggestedLectureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [SuggestedLectureDeleteDialogComponent]
            })
                .overrideTemplate(SuggestedLectureDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SuggestedLectureDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SuggestedLectureService);
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
