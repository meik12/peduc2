/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { PeerUpdateComponent } from 'app/entities/peer/peer-update.component';
import { PeerService } from 'app/entities/peer/peer.service';
import { Peer } from 'app/shared/model/peer.model';

describe('Component Tests', () => {
    describe('Peer Management Update Component', () => {
        let comp: PeerUpdateComponent;
        let fixture: ComponentFixture<PeerUpdateComponent>;
        let service: PeerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [PeerUpdateComponent]
            })
                .overrideTemplate(PeerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PeerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeerService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Peer(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.peer = entity;
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
                    const entity = new Peer();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.peer = entity;
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
