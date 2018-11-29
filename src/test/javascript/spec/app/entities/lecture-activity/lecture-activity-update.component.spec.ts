/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { LectureActivityUpdateComponent } from 'app/entities/lecture-activity/lecture-activity-update.component';
import { LectureActivityService } from 'app/entities/lecture-activity/lecture-activity.service';
import { LectureActivity } from 'app/shared/model/lecture-activity.model';

describe('Component Tests', () => {
    describe('LectureActivity Management Update Component', () => {
        let comp: LectureActivityUpdateComponent;
        let fixture: ComponentFixture<LectureActivityUpdateComponent>;
        let service: LectureActivityService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [LectureActivityUpdateComponent]
            })
                .overrideTemplate(LectureActivityUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(LectureActivityUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LectureActivityService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new LectureActivity(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.lectureActivity = entity;
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
                    const entity = new LectureActivity();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.lectureActivity = entity;
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
