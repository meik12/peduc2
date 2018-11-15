import { NgModule } from '@angular/core';

import { Peduc2SharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [Peduc2SharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [Peduc2SharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class Peduc2SharedCommonModule {}
