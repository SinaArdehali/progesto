import { Component, Input } from '@angular/core';

// webpack html imports
//let template = require('./row-content.component.html');

@Component({
    selector: 'thing',
    templateUrl: './row-content.component.html'

})
export class RowContentComponent {
    @Input() things:any = {};
    @Input() otherThings: any = {};
    constructor() { }

}

