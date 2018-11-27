import { Component } from '@angular/core';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-sim-list',
  template: `
<div>
    <ul class="list-group">
    <h4>Saved Simulations</h4>
    <app-sim-entry *ngFor="let entry of entries" 
        class="list-group-item" [sim]="entry" (click)="removeEntry(entry)"></app-sim-entry>
    </ul>
    <button class="btn mt-2"(click)="newEntry()">AddOne</button>
</div>
`
})
export class SimListComponent {

  name = 'Angular';
  entries = ["1","2","3"];

  nextEntry = 4; // TODO: just for testing

  constructor(private users: UsersService){

  }

  newEntry(): void {
    this.entries.push(""+ this.nextEntry);
    this.nextEntry++;
  }

  removeEntry(entry: string): void {
    let i = this.entries.indexOf(entry);
    if(i >= 0) { this.entries.splice(i, 1);}
  }
}
