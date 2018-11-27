import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-sim-entry',
  template: `Placeholder{{sim}}`
})

export class SimEntryComponent {
  @Input() sim: any;
}
