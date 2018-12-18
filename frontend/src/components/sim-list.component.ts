import { Component } from '@angular/core';
import { ServerCommService } from '../servercomm.service';
import { UsersService } from '../users.service';

import { User, Simulation } from '../models/user';


import {dummyUser} from '../dummy_data';

@Component({
  selector: 'app-sim-list',
  template: `
<div>
    <ul class="list-group">
    <h4>Saved Simulations</h4>
    <app-sim-entry *ngFor="let sim of sims" 
        class="list-group-item" [sim]="sim" (destroy)="removeEntry($event)"></app-sim-entry>
    </ul>
</div>
`
})
export class SimListComponent {
  // states: loading, loaded 
  private sims: Simulation[] = [];

  nextEntry = 4; // TODO: just for testing

  constructor(private users: UsersService, private servercomm: ServerCommService) {
      //this.servercomm.getCurrentUserPromise().then(u => this.onLoadUser(u))
    this.users.sims_p.then(sims => this.onLoadSims(sims));
  }

  onLoadSims(sims: Simulation[]) {
    console.log("Loaded Sims: ")
    console.log(sims);
    this.sims = sims;
  }

  removeEntry(sim: Simulation): void {
    if(this.sims.length) {
      let i = this.sims.indexOf(sim);
      if(i >= 0) { this.sims.splice(i, 1);}
    }

    this.servercomm.reqDeleteSimulation(sim);
  }
}
