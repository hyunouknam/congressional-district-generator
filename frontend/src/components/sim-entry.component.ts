import { Component, Input, Output, EventEmitter } from '@angular/core';

import { Simulation } from '../models/user';

@Component({
  selector: 'app-sim-entry',
  template: 
`
<div class="d-flex">
  <div class="flex-grow-1">SimulationData</div>
  <button type="button" class="close" (click)="destroy.emit(sim)">&times;</button>
</div>
<pre class="m-0">{{sim | json }}<pre>
`,
  styles: ['pre { font-size: 10px}'],
})

export class SimEntryComponent {
  @Input() sim: Simulation;
  @Output() destroy = new EventEmitter<Simulation>();
}
