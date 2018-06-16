/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ProgestoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AttributionTacheDetailComponent } from '../../../../../../main/webapp/app/entities/attribution-tache/attribution-tache-detail.component';
import { AttributionTacheService } from '../../../../../../main/webapp/app/entities/attribution-tache/attribution-tache.service';
import { AttributionTache } from '../../../../../../main/webapp/app/entities/attribution-tache/attribution-tache.model';

describe('Component Tests', () => {

    describe('AttributionTache Management Detail Component', () => {
        let comp: AttributionTacheDetailComponent;
        let fixture: ComponentFixture<AttributionTacheDetailComponent>;
        let service: AttributionTacheService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ProgestoTestModule],
                declarations: [AttributionTacheDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AttributionTacheService,
                    JhiEventManager
                ]
            }).overrideTemplate(AttributionTacheDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AttributionTacheDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttributionTacheService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AttributionTache(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.attributionTache).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
