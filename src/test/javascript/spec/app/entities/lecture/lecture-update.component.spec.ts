/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { LectureUpdateComponent } from 'app/entities/lecture/lecture-update.component';
import { LectureService } from 'app/entities/lecture/lecture.service';
import { Lecture } from 'app/shared/model/lecture.model';

describe('Component Tests', () => {
    describe('Lecture Management Update Component', () => {
        let comp: LectureUpdateComponent;
        let fixture: ComponentFixture<LectureUpdateComponent>;
        let service: LectureService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [LectureUpdateComponent]
            })
                .overrideTemplate(LectureUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(LectureUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LectureService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Lecture(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.lecture = entity;
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
                    const entity = new Lecture();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.lecture = entity;
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
