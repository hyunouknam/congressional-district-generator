import { Component, Input, Output, EventEmitter } from '@angular/core';

import { Simulation } from '../models/user';
import { MapHandlerService } from '../maphandler.service';

@Component({
  selector: 'app-sim-entry',
  template: 
`
<div class="d-flex">
  <div class="flex-grow-1" (click)="showSim()">SimulationData {{sim.id}}, State: {{sim.data[0].state.id}}</div>
  <button type="button" class="close" (click)="destroy.emit(sim)">&times;</button>
</div>
`,
  styles: ['pre { font-size: 10px}'],
})

export class SimEntryComponent {
  @Input() sim: Simulation;
  @Output() destroy = new EventEmitter<Simulation>();

  constructor (private maphandler: MapHandlerService) {
  }

  private showSim() {
    this.maphandler.showSimulation(this.sim);
  }

}
