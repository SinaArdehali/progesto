/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ProgestoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TacheDetailComponent } from '../../../../../../main/webapp/app/entities/tache/tache-detail.component';
import { TacheService } from '../../../../../../main/webapp/app/entities/tache/tache.service';
import { Tache } from '../../../../../../main/webapp/app/entities/tache/tache.model';

describe('Component Tests', () => {

    describe('Tache Management Detail Component', () => {
        let comp: TacheDetailComponent;
        let fixture: ComponentFixture<TacheDetailComponent>;
        let service: TacheService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ProgestoTestModule],
                declarations: [TacheDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TacheService,
                    JhiEventManager
                ]
            }).overrideTemplate(TacheDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TacheDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TacheService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Tache(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.tache).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
