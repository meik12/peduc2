/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Peduc2TestModule } from '../../../test.module';
import { PeerComponent } from 'app/entities/peer/peer.component';
import { PeerService } from 'app/entities/peer/peer.service';
import { Peer } from 'app/shared/model/peer.model';

describe('Component Tests', () => {
    describe('Peer Management Component', () => {
        let comp: PeerComponent;
        let fixture: ComponentFixture<PeerComponent>;
        let service: PeerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [PeerComponent],
                providers: []
            })
                .overrideTemplate(PeerComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PeerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeerService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Peer(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.peers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
