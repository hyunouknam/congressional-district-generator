import { Observable, Observer, fromEvent } from 'rxjs';
import  * as Rx from 'rxjs';
import * as rxOp from 'rxjs/operators';
import { Injectable, EventEmitter } from "@angular/core";

import * as leaflet from 'leaflet'
import { ServerCommService } from './servercomm.service';
import { LayerBacker, MasterPrecinct, MasterDistrict, MasterState, MasterStateInitialJson } from './models/geometry';


// ========== Weird-ass types
//district, precinct, etc that backs a layer
export type LOAD_PHASE = "LOADING_INITIAL" | "LOADING_PRECINCTS" | "FULLY_LOADED" | "ERROR";


// ============ end of types


// Maphandler

@Injectable({ providedIn: 'root', })
export class MapHandlerService {



  /*
  private state: leaflet.LayerGroup;
  private district: leaflet.LayerGroup = leaflet.layerGroup();
  private precinct: leaflet.LayerGroup = leaflet.layerGroup();
   */


  public currentFeatureIn = new EventEmitter<LayerBacker | null>();
  public currentFeature: Observable<LayerBacker | null>;

  constructor(private servercomm: ServerCommService){
    this.currentFeature = this.currentFeatureIn.pipe(rxOp.startWith(null));

  }

    /* // ========= Prints current features as you mouse around for debugging
    // List 
    this.currentFeature.pipe(
        map(e => e == null ? 
            "Nothing selected" : (e as any).feature.properties.precinct_name))
    .subscribe(e => console.log(e));
     */


  //  private highlightFeature(e: leaflet.LeafletEvent) {
  //      let layer: leaflet.FeatureGroup  = e.target;
  //
  //      layer.setStyle({
  //          weight: 5,
  //          color: '#666',
  //          dashArray: '',
  //          fillOpacity: 0.7
  //      });
  //
  //      if (!leaflet.Browser.ie && !leaflet.Browser.opera12 && !leaflet.Browser.edge) {
  //          layer.bringToFront();
  //      }
  //  }
  //
  //  private resetHighlight(e: leaflet.LeafletEvent) {
  //        this.precintGeoJson.resetStyle(e.target);
  //  }
  //
  //  private zoomToFeature(f: leaflet.FeatureGroup) {
  //      this.map.fitBounds(f.getBounds());
  //  }
  //
  //  private fetch(x: Promise<any>) {
  //    x.then( (data: any) => {
  //
  //      let features: Array<{properties:PrecinctProperties}> = data.features;
  //
  //      let handler = (e:any)=>{};
  //      let obs = new Observable<any>((observer: Observer<any>) => {
  //        handler = (e:any) => observer.next(e);
  //      }).pipe(take(10)).subscribe((e:any) => {
  //        //console.log(e);
  //      });
  //
  //      this.precintGeoJson = leaflet.geoJSON(data, {
  //        onEachFeature : (feature, layer) => {
  //          handler([feature, layer]);
  //          layer.on({
  //            mouseover: (e: leaflet.LeafletEvent) => {
  //              this.highlightFeature(e)
  //              this.setCurrentFeature(e.target);
  //            },
  //            mouseout: (...args) =>  this.resetHighlight(...args),
  //            click: (e: leaflet.LeafletEvent) => { 
  //              this.zoomToFeature(e.target);
  //              this.setCurrentFeature(e.target);
  //            },
  //          }); 
  //        }
  //      })
  //
  //      //this.precintGeoJson.addTo(this.precinct);
  //      this.precintGeoJson.addTo(this.map);
  //      
  //      
  //
  //
  //      //====== DEBUGGING
  //      /*
  //      features.slice(0, 10).forEach(feat => {
  //        console.log(feat.properties.id, feat);
  //      });
  //      */
  //      //(window as any).njdata = data;
  //
  //
  //    });
  //  }
  //
  //
  //  public initMapOnElement(elem: HTMLElement) {
  //    //half-assed singleton
  //    // TODO FIX THIS
  //    if(this.map != null) { throw new Error("map already defined in maphandler, should be singleton"); }
  //
  //    console.log(elem);
  //    this.map = leaflet.map(elem).setView([40.19, -74.70], 8);
  //
  //
  //
  //    leaflet.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
  //        id: 'mapbox.light',
  //        attribution:  'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a>' + 
  //                      'contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>,' +
  //                      'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
  //        maxZoom: 18,
  //        accessToken: 'pk.eyJ1IjoicGF1bG5hbTEyMyIsImEiOiJjam1ndzE2bzgzNTJkM3FsazdzOTRtMjMwIn0.Bt5nrR5H2-MeoZkVMhMRJg'
  //    } as any).addTo(this.map);
  //    
  //
  //    
  //    let x = this.fetch_NJ_JSON();
  //    this.fetch(x);
  //
  //    let y = this.fetch_CT_JSON();
  //    this.fetch(y);
  ///*
  //    this.map.on('zoomend', function() {
  //      if (this.map.getZoom() > 8) {
  //        this.map.addLayer(this.precinct);
  //      }
  //      else
  //      {
  //        this.map.removeLayer(this.precinct);
  //      }   
  //    });
  //
  //    this.precinct.addTo(this.map);
  //    */
  //  }
  //
  //
  //
  //  // assuming f has to be a precinct featuregroup
  //  private setCurrentFeature(f: leaflet.FeatureGroup) {
  //    this.currentFeature.emit(f);
  //  }

}




// === TEMP: INITIALIZE MAP

/*
function style() {
    return {
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '',
        fillOpacity: 0.7,
        fillColor: 'gray'
    };
}

function highlight(e) {
    var layer = e.target;

    layer.setStyle({
        weight: 3,
        color: '#666',
        dashArray: '',
        fillOpacity: 1
    });

    layer.bringToFront();

}

let geojson: any;

function resetHighlight(e) {
    geojson.resetStyle(e.target);
}


function onEachFeature(feature: leaflet.FeatureGroup, layer: leaflet.Layer) {
    layer.on({
        mouseover: highlight,
        mouseout: resetHighlight,
        click: zoom
    });
}

geojson = leaflet.geoJSON(statesData, {
    style: style,
    onEachFeature: onEachFeature
}).addTo(map);


 */
