/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { SuggestedLectureUpdateComponent } from 'app/entities/suggested-lecture/suggested-lecture-update.component';
import { SuggestedLectureService } from 'app/entities/suggested-lecture/suggested-lecture.service';
import { SuggestedLecture } from 'app/shared/model/suggested-lecture.model';

describe('Component Tests', () => {
    describe('SuggestedLecture Management Update Component', () => {
        let comp: SuggestedLectureUpdateComponent;
        let fixture: ComponentFixture<SuggestedLectureUpdateComponent>;
        let service: SuggestedLectureService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [SuggestedLectureUpdateComponent]
            })
                .overrideTemplate(SuggestedLectureUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SuggestedLectureUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SuggestedLectureService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SuggestedLecture(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.suggestedLecture = entity;
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
                    const entity = new SuggestedLecture();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.suggestedLecture = entity;
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
