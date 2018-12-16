import { Component, ElementRef, ViewChild } from '@angular/core';

import { MasterDistrict, MasterState, MasterStateInitialJson } from '../models/geometry';

import { MapHandlerService } from '../maphandler.service';
import { ServerCommService } from '../servercomm.service';

import * as leaflet from 'leaflet'

@Component({
  selector: 'app-leaflet',
  template: `
  <div class="h-100 w-100" #mapdiv> </div>
`
})

export class LeafletComponent {
  @ViewChild('mapdiv') mapdiv : ElementRef;
  private map: leaflet.Map;
  private loadPhase: LOAD_PHASE;



  constructor(private servercomm: ServerCommService) {
    this.loadPhase = "LOADING_INITIAL";
  }


  ngAfterViewInit() {
    //Initialize map element
    
    const elem = this.mapdiv.nativeElement

    //half-assed singleton
    // TODO FIX THIS
    if(this.map != null) { throw new Error("map already defined in leaflet component, should be singleton"); }

    //TEMP
    console.log(elem);
    this.map = leaflet.map(elem).setView([40.19, -74.70], 8);



    leaflet.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
        id: 'mapbox.light',
        attribution:  'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a>' + 
                      'contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>,' +
                      'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        accessToken: 'pk.eyJ1IjoicGF1bG5hbTEyMyIsImEiOiJjam1ndzE2bzgzNTJkM3FsazdzOTRtMjMwIn0.Bt5nrR5H2-MeoZkVMhMRJg'
    } as any).addTo(this.map);
    

    
    //TODO: fetch data from maphandler
    //
    
    // ASSIGN EVENTS
    this.map.on('zoomend', () => this.onZoom());

    this.loadData().catch(err => console.error("FAILED TO LOAD GEOM DATA", err));
  }


  // this function will gradually load all the data into this map
  private async loadData() {
    
    // First load all the states
    const statejsons = await this.servercomm.reqInitialGeomData();
    let states = statejsons.map(json => MasterState.loadFromInitialJson(json))

    console.log("LOADED STATES")  
    console.log(states)
    // takes list of masterstates

    //Show all districts on map
    states.forEach(state => {
      state.districts.forEach( district => {
        this.map.addLayer(new leaflet.GeoJSON(district.initialData.geometry))
      });
    });

    this.loadPhase = "LOADING_PRECINCTS";
    console.log("======== NEXT STEP: LOADING PRECINCTS");
    //}).catch(err => console.error("FAILED TO GET INITIAL GEOM DATA", err));
  }

  public onZoom() {
    console.log("Zoom ended")
  }
}


// ========== Weird-ass types
type LOAD_PHASE = "LOADING_INITIAL" | "LOADING_PRECINCTS" | "FULLY_LOADED" | "ERROR";
