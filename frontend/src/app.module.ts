// Core
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';


// Useful Modules
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Ng5SliderModule } from 'ng5-slider';

// Child components
import { SimParamsComponent } from './components/sim-params.component';
import { SimListComponent } from './components/sim-list.component';
import { SimEntryComponent } from './components/sim-entry.component';
import { CurrentUserComponent } from './components/current-user.component';
import { LeafletComponent } from './components/leaflet.component';
import { LeafletInfoComponent } from './components/leaflet-info.component';

// Services
import { MapHandlerService } from './maphandler.service';
import { ServerCommService } from './servercomm.service';

@NgModule({
    imports: [BrowserModule, NgbModule, Ng5SliderModule, FormsModule, HttpClientModule],
    providers: [ MapHandlerService, ServerCommService ],
    declarations: [
        SimListComponent, SimEntryComponent, CurrentUserComponent,
        SimParamsComponent, LeafletComponent, LeafletInfoComponent
    ],
    bootstrap: [SimParamsComponent, SimListComponent, CurrentUserComponent, LeafletComponent, LeafletInfoComponent]
})

export class AppModule {
}
