import { Component, ElementRef, ViewChild } from '@angular/core';

import { Repo, GeoRegion, StateMap, DistrictForMap,
        MasterPrecinct, MasterDistrict, MasterState, MasterStateJson } from '../models/geometry';

import { MapHandlerService } from '../maphandler.service';
import { ServerCommService } from '../servercomm.service';

import * as L from 'leaflet'
import * as Topo from 'topojson-client';

import * as GEOM from '../models/geometry';
(window as any).G = GEOM
//debugging


const DEFAULT_STYLE = {
  weight:1,
  color: '#66d',
  dashArray: '',
  fillOpacity: 0.1
}

const HIGHLIGHT_STYLE= {
  weight: 5,
  color: '#666',
  dashArray: '',
  fillOpacity: 0.7
}
@Component({
  selector: 'app-leaflet',
  template: `
  <div class="h-100 w-100" #mapdiv> </div>
`
})

export class LeafletComponent {
  @ViewChild('mapdiv') mapdiv : ElementRef;
  private map: L.Map;



  //only valid during LOADING_PRECINCTS
  //maps precintid to district id
  private tempPrecinctDistrictMap: Map<string, string>|null = null;


  constructor(private maphandler: MapHandlerService, private servercomm: ServerCommService) {
    (window as any).LC = this;
    (window as any).Topo = Topo;
  }


  ngAfterViewInit() {
    //Initialize map element
    

    // TODO do we need this?
    if(this.map != null) { throw new Error("map already defined in leaflet component, should be singleton"); }

    //initialize element with leaflet map
    const elem = this.mapdiv.nativeElement
    this.map = L.map(elem, {
      preferCanvas: true,
    });
    this.map.setView([40.19, -74.70], 8);

    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
        id: 'mapbox.light',
        attribution:  'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a>' + 
                      'contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>,' +
                      'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        accessToken: 'pk.eyJ1IjoicGF1bG5hbTEyMyIsImEiOiJjam1ndzE2bzgzNTJkM3FsazdzOTRtMjMwIn0.Bt5nrR5H2-MeoZkVMhMRJg'
    } as any).addTo(this.map);
    

    // ASSIGN EVENTS
    this.map.on('zoomend', () => this.onZoom());


    //Now that map is intialized, show a state on 
    this.maphandler.dataLoad.then(() => {
      this.displayStateMap(Repo.states.values().next().value.defaultMap, "DISTRICT")
    })
  }


  public onZoom() {
    console.log("Zoom ended")
  }



  // ================= LEAFLET INTERACTION
  
  private handleMouseOver(e: L.LeafletEvent) {
    var layer = e.target;
    
    const backer:GeoRegion|undefined = Repo.layers.get(layer);
    if(backer == undefined) {
      throw Error("Layer has no backer: " +  JSON.stringify(layer));
    } else if (backer instanceof DistrictForMap) {
      console.log("Moused over dist: " + backer.id);
    } else if (backer instanceof MasterPrecinct) {
      console.log("Moused over precinct: " + backer.id);
    } else {
      throw Error("Layer has unknown backer: " +  JSON.stringify(backer));
    }

    layer.setStyle(HIGHLIGHT_STYLE);

    this.maphandler.currentFeatureIn.emit(backer);
  }

  private handleMouseOut(e: L.LeafletEvent) {
    var layer = e.target;
    
    const backer:GeoRegion|undefined = Repo.layers.get(layer);
    if(backer == undefined) {
      throw Error("Layer has no backer: " +  JSON.stringify(layer));
    } else if (backer instanceof DistrictForMap) {
      console.log("Moused out dist: " + backer.id);
    } else if (backer instanceof MasterPrecinct) {
      console.log("Moused out precinct: " + backer.id);
    } else {
      throw Error("Layer has unknown backer: " +  JSON.stringify(backer));
    }

    layer.setStyle(DEFAULT_STYLE)
  }



  // map helpers 
  // ========= MAP CONTROLLER? Interaction ideas
  // new type: LayerHaver: thing that has a layer
  //
  // each state can have a list of LayerHavers currently displayed
  // if we want to change what is rendered for a given state, we should
  // give it a new list of things to render
  //
  //
  // At any given time, we can render one or more maps (from separate states)
  // at one of two (three?) zoom levels: districts or precincts
  // 
  // members:
  //  currentMap, LOD = "DISTRICT" | "PRECINCT"
  // functions:
  //   displayStateMap(map, level_of_detail): hides all other maps, shows this one at the desired level
  //    
  
  private currentMap: StateMap | null = null;
  private currentLOD: LEVEL_OF_DETAIL = "DISTRICT";
  private displayStateMap(map: StateMap, lod: LEVEL_OF_DETAIL) {
    //If nothing needs to change then change nothing
    if (this.currentMap == map && this.currentLOD == lod) { return; }


    this.clearDisplay();

    const layers: L.Polygon[] = [];

    switch (lod) {
      case "DISTRICT":
        for(const d4m of map.districts4map.values()) {
          layers.push(d4m.layer);
        }
        break;
      case "PRECINCT":
        map.state.precincts.forEach(mp => {
          layers.push(mp.layer);
        });
        break;
    }

    //set styles on layers
    layers.forEach(l => {
      l.addTo(this.map);
      l.setStyle(DEFAULT_STYLE);
      l.on('mouseover', e => this.handleMouseOver(e));
      l.on('mouseout', e => this.handleMouseOut(e));
    });
      

    this.currentMap = map;
    this.currentLOD = lod;
  }

  // removes whatever is displayed from the map
  private clearDisplay() {
    if(this.currentMap == null) { return; }

    switch (this.currentLOD) {
      case "DISTRICT":
        for(const d4m of this.currentMap.districts4map.values()) {
          d4m.layer.removeFrom(this.map);
        }
        //TODO
        break;
      case "PRECINCT":
        this.currentMap.state.precincts.forEach(mp => {
          mp.layer.removeFrom(this.map);
        });
        break;
    }

    this.currentMap = null;
  }


}


type LEVEL_OF_DETAIL = "DISTRICT" | "PRECINCT"
