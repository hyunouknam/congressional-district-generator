import { Component } from '@angular/core';
import { MapHandlerService } from '../maphandler.service';
import { MasterState, Repo } from '../models/geometry';

    //<ul class="list-group">

    //<h4>Saved Simulations</h4>
    //<app-sim-entry *ngFor="let sim of sims" 
    //    class="list-group-item" [sim]="sim" (destroy)="removeEntry($event)"></app-sim-entry>
    //</ul>

@Component({
  selector: 'app-state-selector',
  template: `
<div>
<button type="button" class="btn btn-outline-secondary mr-2" *ngFor="let state of states" (click)="clicked(state)">View {{state}} </button>
</div>
`
})
export class StateSelectorComponent {
  private states: string[] = [];

  constructor(private maphandler: MapHandlerService){
    this.maphandler.dataLoad.then(() => this.onDataLoad());
  }

  //once states are loaded, make buttons for em
  private onDataLoad() {
    for(const state of Repo.states.values()) {
      this.states.push(state.id);
    }
  }

  private clicked(stateId: string) {
    this.maphandler.showState(stateId);
  }

}
