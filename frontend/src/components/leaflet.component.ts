import { Component, ElementRef, ViewChild } from '@angular/core';

import { MasterDistrict, MasterState, MasterStateInitialJson } from '../models/geometry';

import { MapHandlerService, LOAD_PHASE, LayerBacker } from '../maphandler.service';
import { ServerCommService } from '../servercomm.service';

import * as L from 'leaflet'



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
  private loadPhase: LOAD_PHASE;

  private stateRepo: Map<string, MasterState>|null = null;
  private districtRepo: Map<string, MasterDistrict>|null = null;

  //map from layer to the District,precinct, etc, it refers to
  private layerRepo: Map<L.Layer, LayerBacker> = new Map();


  constructor(private maphandler: MapHandlerService, private servercomm: ServerCommService) {
    this.loadPhase = "LOADING_INITIAL";
    console.log("barbop");
    (window as any).G = this;
  }


  ngAfterViewInit() {
    //Initialize map element
    

    // TODO do we need this?
    if(this.map != null) { throw new Error("map already defined in leaflet component, should be singleton"); }

    //initialize element with leaflet map
    const elem = this.mapdiv.nativeElement
    this.map = L.map(elem).setView([40.19, -74.70], 8);

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
    //Load adata
    this.loadData().catch(err => console.error("FAILED TO LOAD GEOM DATA", err));
  }


  // this function will gradually load all the data into this map
  private async loadData() {
    
    // First load all the states
    const statejsons = await this.servercomm.reqInitialGeomData();
    let states = statejsons.map(json => MasterState.loadFromInitialJson(json))

    console.log("LOADED STATES")  
    this.handleInitialLoad(states)
    // takes list of masterstates

    this.loadPhase = "LOADING_PRECINCTS";
    console.log("======== NEXT STEP: LOADING PRECINCTS");
    //}).catch(err => console.error("FAILED TO GET INITIAL GEOM DATA", err));
  }

  public onZoom() {
    console.log("Zoom ended")
  }


  // Stores states, adds districts as layers to 
  private handleInitialLoad(states: MasterState[]) {
    console.log(states)
    this.stateRepo = new Map();
    this.districtRepo = new Map();

    //Show all districts on map
    states.forEach(state => {
      this.stateRepo!.set(state.id, state);

      state.districts.forEach( district => {
        this.districtRepo!.set(district.id, district);

        const layer = GeoJsonToPolygon(district.initialData.geometry)
        layer.setStyle(DEFAULT_STYLE);
        district.initialData.layer = layer;
        this.layerRepo.set(layer, district)

        this.map.addLayer(layer);
        layer.on('mouseover', e => this.handleMouseOver(e));
        layer.on('mouseout', e => this.handleMouseOut(e));
      });
    });

  }



  // ================= LEAFLET INTERACTION
  
  private handleMouseOver(e: L.LeafletEvent) {
    var layer = e.target;
    
    const backer:LayerBacker|undefined = this.layerRepo.get(layer);
    if(backer == undefined) {
      throw Error("Layer has no backer: " +  JSON.stringify(layer));
    } else if (backer instanceof MasterDistrict) {
      console.log("Moused over dist: " + backer.id);
    } else {
      throw Error("Layer has unknown backer: " +  JSON.stringify(backer));
    }

    layer.setStyle(HIGHLIGHT_STYLE);

    this.maphandler.currentFeatureIn.emit(backer);
  }

  private handleMouseOut(e: L.LeafletEvent) {
    var layer = e.target;
    
    const backer:LayerBacker|undefined = this.layerRepo.get(layer);
    if(backer == undefined) {
      throw Error("Layer has no backer: " +  JSON.stringify(layer));
    } else if (backer instanceof MasterDistrict) {
      console.log("Moused out dist: " + backer.id);
    } else {
      throw Error("Layer has unknown backer: " +  JSON.stringify(backer));
    }

    layer.setStyle(DEFAULT_STYLE)
  }
}


//takes a geojson polygon or multipolygon and returns a leaflet polygon layer
function GeoJsonToPolygon(g: any): L.Polygon {
  if(g.type == "Polygon"){
    g.coordinates.forEach((ring:Array<number[]>) => {
      ring.forEach(coord => {
        coord.reverse()
      });
    });
  } else if (g.type == "MultiPolygon") {
    g.coordinates.forEach((polygon:Array<Array<number[]>>) => {
      polygon.forEach(ring => {
        ring.forEach(coord => {
          coord.reverse()
        });
      });
    });
  } else {
    throw Error("geometry is not polygonal: \n" + JSON.stringify(g));
  }
  return new L.Polygon(g.coordinates);
}

