/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Peduc2TestModule } from '../../../test.module';
import { PeerDetailComponent } from 'app/entities/peer/peer-detail.component';
import { Peer } from 'app/shared/model/peer.model';

describe('Component Tests', () => {
    describe('Peer Management Detail Component', () => {
        let comp: PeerDetailComponent;
        let fixture: ComponentFixture<PeerDetailComponent>;
        const route = ({ data: of({ peer: new Peer(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Peduc2TestModule],
                declarations: [PeerDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PeerDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PeerDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.peer).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
