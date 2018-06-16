/**
 * Created by sardehali on 02/03/18.
 */
import {Component, OnInit, Input, OnDestroy,ViewChild, ViewEncapsulation} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { ActiviteService } from './activite.service';

 import { ITEMS_PER_PAGE, ResponseWrapper } from '../shared';



import {
    FormBuilder,
    FormControl,
    FormGroup,
    Validators
} from '@angular/forms';

import * as moment from 'moment';
 import {Principal} from "../shared/auth/principal.service";
import {Tache} from "../entities/tache/tache.model";
import {AttributionTache} from "../entities/attribution-tache/attribution-tache.model";




@Component({
    selector: 'jhi-activite',
    encapsulation: ViewEncapsulation.None,
    templateUrl: './activite.component.html',
    styleUrls: [
        'activite.component.css'
    ]
})
export class ActiviteComponent implements OnInit{

    currentAccount: any;
    eventSubscriber: Subscription;
    show = false;

    monIntDeTest : number;

    tachesCurrentUsers: Tache[];



    public date = moment();
    public dateForm: FormGroup;

    public isReserved = null;

    public daysArr;











    constructor(
        private fb: FormBuilder,
        private alertService: JhiAlertService,
        protected eventService: ActiviteService,
        private eventManager: JhiEventManager
    ) {
        this.initDateRange();
    }






    public initDateRange() {
        return (this.dateForm = this.fb.group({
            dateFrom: [null, Validators.required],
            dateTo: [null, Validators.required]
        }));
    }

    public ngOnInit() {
        this.daysArr = this.createCalendar(this.date);
        this.monIntDeTest = 42;
        // ci dessous , je recupere le login de la personne qui est connectée
        //console.log("*/-*/-"+localStorage.getItem("login").replace("\"", ""));
        this.loadAll();
    }

    public loadAll(){

        this.eventService.getTachesCurrentUser(localStorage.getItem("login").replace(/\"/g, "")).subscribe(
            (res: ResponseWrapper) => {
                this.tachesCurrentUsers = res.json;
                // jitere a travers ce tableau dobjet projet pour essayer de mettre tous les
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    public createCalendar(month) {
        let firstDay = moment(month).startOf('M');
        let days = Array.apply(null, { length: month.daysInMonth() })
            .map(Number.call, Number)
            .map(n => {
                return moment(firstDay).add(n, 'd');
            });

        for (let n = 0; n < firstDay.weekday(); n++) {
            days.unshift(null);
        }
        return days;
    }

    public nextMonth() {
        this.date.add(1, 'M');
        this.daysArr = this.createCalendar(this.date);

    }

    public previousMonth() {
        this.date.subtract(1, 'M');
        this.daysArr = this.createCalendar(this.date);
    }

    public todayCheck(day) {
        if (!day) {
            return false;
        }
        return moment().format('L') === day.format('L');
    }

    public reserve() {
        if (!this.dateForm.valid) {
            return;
        }
        let dateFromMoment = this.dateForm.value.dateFrom;
        let dateToMoment = this.dateForm.value.dateTo;
        this.isReserved = `Reserved from ${dateFromMoment} to ${dateToMoment}`;
    }

    public isSelected(day) {
        if (!day) {
            return false;
        }
        let dateFromMoment = moment(this.dateForm.value.dateFrom, 'MM/DD/YYYY');
        let dateToMoment = moment(this.dateForm.value.dateTo, 'MM/DD/YYYY');
        if (this.dateForm.valid) {
            return (
                dateFromMoment.isSameOrBefore(day) && dateToMoment.isSameOrAfter(day)
            );
        }
        if (this.dateForm.get('dateFrom').valid) {
            return dateFromMoment.isSame(day);
        }
    }

    public selectedDate(day) {
        let dayFormatted = day.format('MM/DD/YYYY');
        if (this.dateForm.valid) {
            this.dateForm.setValue({ dateFrom: null, dateTo: null });
            return;
        }
        if (!this.dateForm.get('dateFrom').value) {
            this.dateForm.get('dateFrom').patchValue(dayFormatted);
        } else {
            this.dateForm.get('dateTo').patchValue(dayFormatted);
        }
    }







    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    refresh(): void {
        window.location.reload();
    }







}
