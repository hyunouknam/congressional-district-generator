import { Component } from '@angular/core';
import { ServerCommService } from '../servercomm.service';

import { User, Simulation } from '../models/user';



@Component({
  selector: 'app-sim-list',
  template: `
<div>
    <ul class="list-group">
    <h4>Saved Simulations</h4>
    <app-sim-entry *ngFor="let sim of (user? user.simulations : [])" 
        class="list-group-item" [sim]="sim" (destroy)="removeEntry($event)"></app-sim-entry>
    </ul>
</div>
`
})
export class SimListComponent {
  // states: loading, loaded 
  user : User | null;

  nextEntry = 4; // TODO: just for testing

  constructor(private servercomm: ServerCommService){
      this.servercomm.getCurrentUserPromise().then(u => (this.user = u || null));
  }

  removeEntry(sim: Simulation): void {
    if(this.user) {
      let i = this.user.simulations.indexOf(sim);
      if(i >= 0) { this.user.simulations.splice(i, 1);}
    }

    this.servercomm.reqDeleteSimulation(sim);
  }
}
