import { NgModule, LOCALE_ID } from '@angular/core';
import { DatePipe, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import locale from '@angular/common/locales/th';

@NgModule({
    imports: [HttpClientModule],
    exports: [],
    declarations: [],
    providers: [
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'th'
        },
        DatePipe
    ]
})
export class WildboarCoreModule {
    constructor() {
        registerLocaleData(locale);
    }
}
