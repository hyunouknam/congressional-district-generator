import { Component } from '@angular/core';
import { ServerCommService } from '../servercomm.service';

import { User, Simulation } from '../models/user';


import {dummyUser} from '../dummy_data';

@Component({
  selector: 'main.main-app',
  template: `
        <app-state-selector id="stateselector"></app-state-selector>
        <app-leaflet class="border rounded" id="map"></app-leaflet>
        <app-leaflet-info class="border rounded p-3" id="infobar">
        </app-leaflet-info>
        <app-sim-params class="border rounded p-3" id="js-params">
        </app-sim-params>
        <app-sim-list class="border rounded p-3" id="js-simulations">
        </app-sim-list>
`,
 styles: [`
/* For the main map page, use a specific grid to show all the panes*/
:host {
    display: grid;
    grid-template-columns: 4fr 2fr ;
    grid-template-rows: min-content 2fr 2fr min-content;
    grid-template-areas: "stateselector none"
                         "map info"
                         "map simulations"
                         "params params";
    grid-gap: 1em;
}
 #stateselector { grid-area: stateselector; }
 #map { grid-area: map; }
 #infobar { grid-area: info; }
 #js-params { grid-area: params; }
 #js-simulations { grid-area: simulations; }
`] 
})
export class AppMainComponent {

  constructor() {
  }

}
