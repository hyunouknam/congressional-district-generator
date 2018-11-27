import { Component, ElementRef, ViewChild } from '@angular/core';

import { MapHandlerService } from '../maphandler.service';

@Component({
  selector: 'app-leaflet',
  template: `
  <div class="h-100 w-100" #mapdiv> </div>
`
})
export class LeafletComponent {
  @ViewChild('mapdiv') mapdiv : ElementRef;

  constructor(private maphandler: MapHandlerService) {
  }

  ngAfterViewInit() {
    this.maphandler.initMapOnElement(this.mapdiv.nativeElement); //temp! later do it by element reference
  }
}


