import { Observable, Observer, fromEvent } from 'rxjs';
import  * as Rx from 'rxjs';
import * as rxOp from 'rxjs/operators';
import { Injectable, EventEmitter } from "@angular/core";

import * as leaflet from 'leaflet'
import { ServerCommService } from './servercomm.service';
import { Repo, GeoRegion, MasterPrecinct, MasterDistrict, MasterState, MasterStateJson } from './models/geometry';
import { Simulation } from './models/user';
import * as Geometry from './models/geometry';


// ========== Weird-ass types
//district, precinct, etc that backs a layer
export type MapAction = ["STATE"|"SIM", string | Simulation];

// ============ end of types


// Maphandler

@Injectable({ providedIn: 'root', })
export class MapHandlerService {



  /*
  private state: leaflet.LayerGroup;
  private district: leaflet.LayerGroup = leaflet.layerGroup();
  private precinct: leaflet.LayerGroup = leaflet.layerGroup();
   */


  // leaflet events pipe into here, so that other components can use them
  public currentFeatureIn = new EventEmitter<GeoRegion | null>();
  //observe this one 
  public currentFeature: Observable<GeoRegion | null>;

  // fulfilled once geom data is loaded
  public dataLoad: Promise<void>;


  //will emit an action whenever something wants to change what's shown on the map
  public mapActionEmitter = new EventEmitter<MapAction>();
  

  constructor(private servercomm: ServerCommService){
    this.currentFeature = this.currentFeatureIn.pipe(rxOp.startWith(null));


    //Initiate loading of geometry data
    this.dataLoad = this.loadDataAlternate()
  }

  private async loadDataAlternate() {
    console.log("Loading initial data")
    const statejsons = await this.servercomm.reqInitialGeomData();
    const state_ids = statejsons.map(sj => sj.id);

    const topojsons:{[s:string]:any} = {};
    for( const sid of state_ids) {
      console.log(`Loading topo file for ${sid}`)
      topojsons[sid] = await this.servercomm.reqStateTopoJson(sid);
    }

    (window as any).ct_topo = topojsons['CT'] //TODO TEMP DEBUG

    // Set repo only once data is loaded, so anyone who fucks it up is rip
    Repo.states = new Map<string, MasterState>();
    Repo.districts = new Map<string, MasterDistrict>();
    Repo.precincts = new Map<string, MasterPrecinct>();
    Repo.layers = new Map<L.Layer, GeoRegion>();


    // load geoms from fetched jsons
    for(const statejson of statejsons){
      const statetopo = topojsons[statejson.id]
      MasterState.loadFromInitialJson(statejson, statetopo);
    }

    console.log("Initialized all masterStates");
  }

  // ======== PUBLIC API

  public showSimulation(sim: Simulation) { this.mapActionEmitter.emit(["SIM", sim]); }
  public showState(stateId: string) { this.mapActionEmitter.emit(["STATE", stateId]); }

}
