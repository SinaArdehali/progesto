/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ProgestoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProjetDetailComponent } from '../../../../../../main/webapp/app/entities/projet/projet-detail.component';
import { ProjetService } from '../../../../../../main/webapp/app/entities/projet/projet.service';
import { Projet } from '../../../../../../main/webapp/app/entities/projet/projet.model';

describe('Component Tests', () => {

    describe('Projet Management Detail Component', () => {
        let comp: ProjetDetailComponent;
        let fixture: ComponentFixture<ProjetDetailComponent>;
        let service: ProjetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ProgestoTestModule],
                declarations: [ProjetDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProjetService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProjetDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjetDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Projet(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.projet).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
