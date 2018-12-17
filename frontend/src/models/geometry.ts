import * as L from 'leaflet';
import * as Topo from 'topojson-client';


// Holds all objects that have unique ids, as well as a few extra mappings
export class Repo {
  static states:    Map<string, MasterState>    = new Map();
  static districts: Map<string, MasterDistrict> = new Map();
  static precincts: Map<string, MasterPrecinct> = new Map();

  //map from layer to the District,precinct, etc, it refers to
  static layers: Map<L.Layer, GeoRegion> = new Map();
}

//TODO TEMP DEBUG
(window as any).R = Repo


// =================================================================
//     A TREATISE ON THE INSTANTIATION OF THE FOLLOWING OBJECTS:
//
// All of the following should only ever be created by the loadFromJson methods
// Furthermore, they should only every be created in the following way:
//
// MasterStates are loaded from initialdata json + corresponding StateTopoJson
// this initializes masterdistricts, masterprecincts, defaultmap, districts4map
// also adds all of them to repo
//

// =========== Define all json formats
//
export type GeoJson = any;

//TODO: outdated?
export type GeoDataJson = {
  geometry: GeoJson;
  population: number;
  average_democrat_votes: number;
}

export type PrecinctTopoEntry = {
  id: string;
  properties: {
    name: string;
    population: number;
    average_democrat_votes: number;
  }
}


export type MasterDistrictJson = {
  name: string;
  id: string;
}

export type MasterStateJson = {
  name: string;
  id: string;
  districts: MasterDistrictJson[];
  default_map: StateMapJson;
}

export type StateMapJson = { [dist_Id:string]: string[]}

export type StateTopoJson = {
  type: "Topology";
  objects: {
    precincts: {
      type: "GeometryCollection";
      geometries: Array<PrecinctTopoEntry>;
    }
  }
  arcs: any[];
  bbox?: any;
  transform?: any;
}


// =============== Classes


export interface GeoRegion {
  readonly geometry: GeoJson;
  readonly layer: L.Polygon;
  readonly name: string;
  readonly population: number;
  readonly average_democrat_votes: number;
}


export class MasterPrecinct implements GeoRegion{
  private constructor(
    public readonly name: string, 
    public readonly id: string, 
    public readonly state: MasterState,
    public readonly topoJsonEntry: PrecinctTopoEntry ) {
  }

  public static loadFromJson(json: PrecinctTopoEntry, state: MasterState){
    const mp = new MasterPrecinct(json.properties.name, json.id, state, json);
    Repo.precincts.set(mp.id, mp);

    //const layer = GeoJsonToPolygon(mp.data.geometry);
    //mp.data.layer = layer;
    //Repo.layers.set(layer, mp)

    return mp;
  }

  private cache_layer: L.Polygon|null = null;
  private cache_geometry: GeoJson|null = null;
  get geometry() { this.cache_geometry = this.cache_geometry || 
      Topo.feature(this.state.topo as any, this.topoJsonEntry as any);
      return this.cache_geometry;}

  get layer() { 
    if(this.cache_layer==null) {
      this.cache_layer = GeoJsonToPolygon(this.geometry); 
      Repo.layers.set(this.cache_layer, this);
    }
    return this.cache_layer; 
  }

  get population() { return this.topoJsonEntry.properties.population; }
  get average_democrat_votes() { return this.topoJsonEntry.properties.average_democrat_votes; }
}

export class MasterDistrict {
  private constructor(
    public readonly name: string, 
    public readonly id: string, 
    public readonly state: MasterState,) { 
  }

  public static loadFromInitialJson(json: MasterDistrictJson, state:MasterState){
    const md =  new MasterDistrict(json.name, json.id, state);
    Repo.districts.set(md.id, md);

    //TODO generate layer?
    return md;
  }
}


export class MasterState {
  public districts: MasterDistrict[] = [];
  public precincts: MasterPrecinct[] = [];
  public defaultMap: StateMap;
  public topo: StateTopoJson;

  private constructor(public name: string, public id: string) {
  }

  public static loadFromInitialJson(json: MasterStateJson, topo: StateTopoJson) {
    let ms = new MasterState(json.name, json.id);
    ms.topo = topo;
    Repo.states.set(ms.id, ms);

    //add all districts
    json.districts.forEach(distJson => {
      let md = MasterDistrict.loadFromInitialJson(distJson, ms);
      ms.districts.push(md)
    })

    //add all precincts
    topo.objects.precincts.geometries.forEach(geom => {
      const mp = MasterPrecinct.loadFromJson(geom, ms);
      ms.precincts.push(mp);
    });

    //add defaultMap
    let map = StateMap.loadFromJson(json.default_map,ms);
    ms.defaultMap = map;

    return ms;
  }

  public toString() {
    return `<MasterDistrict: ${this.name}, ${this.id}, Districts:
    ${this.districts.length} of them
>`
  }
}



export class DistrictForMap implements GeoRegion{
  public constructor(
    public readonly map: StateMap, 
    public readonly id: string, ) { //note, masterDistrict data is only initial data
  }

  get master():MasterDistrict { return Repo.districts.get(this.id)!; }

  //discards cached stuff
  public dirty() {
    throw Error("Dist4Map Dirty() not implemented")
  }

  // ================= GeoRegion implementation
  
  private cache_layer: L.Polygon|null = null;
  private cache_geometry: GeoJson|null = null;

  get geometry() { 
    if(this.cache_geometry == null) {
      const prec_geoms: PrecinctTopoEntry[] = [];
      this.map.d_p_get(this.id)!.forEach(pId => {
        prec_geoms.push(Repo.precincts.get(pId)!.topoJsonEntry);
      });

      this.cache_geometry = Topo.merge(this.master.state.topo as any, prec_geoms as any);
    }
    return this.cache_geometry;
  }

  get layer() { 
    if(this.cache_layer==null) {
      this.cache_layer = GeoJsonToPolygon(this.geometry); 
      Repo.layers.set(this.cache_layer, this);
    }
    return this.cache_layer; 
  }

  get name() { return this.master.name}
  get population() { return -999; }
  get average_democrat_votes() { return -999; }
}

//StateMap can only be made
export class StateMap {
  //TODO: we should get a null district from the server


  //precinctid to districtid
  private p_d_map = new Map<string, string>();

  //districtid to precinctid[]
  private d_p_map = new Map<string, Set<string>>();

  //districtid to districtformap
  public districts4map = new Map<string, DistrictForMap>();

  //constructor starts by setting all precincts to null district
  public constructor(public readonly state: MasterState) {
    state.precincts.forEach(mp => {
      this.p_d_map.set(mp.id, "NULL");
    });

    state.districts.forEach(md => {
      this.d_p_map.set(md.id, new Set());
    })
    this.d_p_map.set("NULL", new Set(state.precincts.map(mp => mp.id)));
  }

  public static loadFromJson(json: StateMapJson, state: MasterState){
    const map = new StateMap(state);

    //Assing precincts from mapping
    for(const distId of Object.keys(json)) {
      for(const precId of json[distId]) {
        map.set_p_d(precId,distId);
      } }

    //Make district4map objects
    for(const distId of Object.keys(json)) {
      map.districts4map.set(distId, new DistrictForMap(map, distId));
    //this.districts4map.set(md.id, new DistrictForMap(this, md.id, md.data));
    }
    
    return map;
  }

  //assigns prec to specified district
  public set_p_d(pId:string, dId: string) {
    if(!(this.p_d_map.has(pId) && this.d_p_map.has(dId))){
      throw Error(`Invalid precint or district ids: '${pId}', '${dId}'`);
    }

    const oldD = this.p_d_map.get(pId);
    //move precinct from old dist to new
    this.d_p_map.get(oldD!)!.delete(pId);
    this.d_p_map.get(dId)!.add(pId);

    //assign prec to dist
    this.p_d_map.set(pId, dId);

    //dirty all affected districts TODO, NOT YET IMPLEMENTED SAFELY
    const d4m_old = this.districts4map.get(oldD!);
    const d4m_new = this.districts4map.get(dId);
    d4m_old && d4m_old.dirty();
    d4m_new && d4m_new.dirty();
  }

  public d_p_get(dId: string) { return this.d_p_map.get(dId); }
  public p_d_get(pId: string) { return this.p_d_map.get(pId); }


  public toString() {
    const vals: { [s:string]: string[]} = {};
    for (const [dId, precincts] of this.d_p_map.entries()) {
      vals[dId] = Array.from(precincts)
    }

    const s = `Map for ${this.state.id} {` +
              JSON.stringify(vals) + "\n}\n";

    //test that both mapping match up
    for(const [dId, v] of this.d_p_map.entries()) {
      for(const pId of v) {
        const dId2 = this.p_d_map.get(pId);
        if(dId2 != dId){
          console.error(s);
          throw Error(`StateMap mismatch error: p<${pId}> is in both d<${dId}> and d<${dId2}>`);
    } } }
    return s;
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
  } else if (g.type == "Feature") {
    return GeoJsonToPolygon(g.geometry);
  } else {
    throw Error("geometry is not polygonal: \n" + JSON.stringify(g));
  }
  return new L.Polygon(g.coordinates);
}
