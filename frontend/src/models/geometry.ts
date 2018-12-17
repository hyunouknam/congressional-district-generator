import * as L from 'leaflet';

export type LayerBacker = MasterDistrict | MasterPrecinct; // | MasterPrecinct or ...

// Holds all objects that have unique ids, as well as a few extra mappings
export class Repo {
  static states:    Map<string, MasterState>    = new Map();
  static districts: Map<string, MasterDistrict> = new Map();
  static precincts: Map<string, MasterPrecinct> = new Map();

  //map from layer to the District,precinct, etc, it refers to
  static layers: Map<L.Layer, LayerBacker> = new Map();
}

//TODO TEMP DEBUG
(window as any).R = Repo


// =================================================================
//     A TREATISE ON THE INSTANTIATION OF THE FOLLOWING OBJECTS:
//
// All of the following should only ever be created by the loadFromJson methods
// Furthermore, they should only every be created in the following way:
//
// MasterStates are loaded from initialdata json, which in turn creates districts
//
// MasterPrecincts are subsequently loaded one at a time, which automatically connects them
// to their state
//
// A StateMap should be created and assigned to each MasterState's defaultMap
// Statemap should then be filled in by calling set_p_d
//
//
// StateMap also needs district data. (geoms, pop)
// Everything is terrible, so fill those in by calling setDistrictData
// 
//
// TODO: JESUS CHRIST this oughta be simpler

export type GeoJson = any;

export type GeoDataJson = {
  geometry: GeoJson;
  population: number;
  average_democrat_votes: number;
}



export type MasterPrecinctJson = {
  name: string;
  id: string;
  data: GeoDataJson;
}

export type MasterDistrictInitialJson = {
  name: string;
  id: string;
  initialData: GeoDataJson;
}

export type MasterStateInitialJson = {
  name: string;
  id: string;
  districts: MasterDistrictInitialJson[];
}

export class GeoData {
  public layer: L.Polygon|null = null; //TODO TEMP

  public constructor(
    public readonly geometry: GeoJson,
    public readonly population: number,
    public readonly average_democrat_votes: number,
  ){}

  public static loadFromJSON(json: GeoDataJson) {
    return new GeoData(json.geometry, json.population, json.average_democrat_votes);
  }
}




export class MasterPrecinct {
  private constructor(
    public readonly name: string, 
    public readonly id: string, 
    public readonly state: MasterState,
    public readonly data: GeoData) {
  }

  public static loadFromJson(json: MasterPrecinctJson, state: MasterState){
    let geodat = GeoData.loadFromJSON(json.data);
    const mp = new MasterPrecinct(json.name, json.id, state, geodat);

    Repo.precincts.set(mp.id, mp);
    state.precincts.push(mp);

    const layer = GeoJsonToPolygon(mp.data.geometry);
    mp.data.layer = layer;
    Repo.layers.set(layer, mp)

    return mp;
  }
}

export class MasterDistrict {
  private constructor(
    public readonly name: string, 
    public readonly id: string, 
    public readonly state: MasterState,
    public readonly data: GeoData) { //note, masterDistrict data is only initial data
  }

  public static loadFromInitialJson(json: MasterDistrictInitialJson, state:MasterState){
    let geodat = GeoData.loadFromJSON(json.initialData);
    const md =  new MasterDistrict(json.name, json.id, state, geodat);
    Repo.districts.set(md.id, md);

    const layer = GeoJsonToPolygon(md.data.geometry)
    md.data.layer = layer;
    Repo.layers.set(layer, md)

    return md;
  }
}

export class DistrictForMap {
  public constructor(
    public readonly map: StateMap, 
    public readonly id: string, 
    public readonly data: GeoData) { //note, masterDistrict data is only initial data
  }
}

export class MasterState {
  public districts: MasterDistrict[] = [];
  public precincts: MasterPrecinct[] = [];
  public defaultMap: StateMap;

  private constructor(public name: string, public id: string) {
  }

  public static loadFromInitialJson(json: MasterStateInitialJson) {
    let ms = new MasterState(json.name, json.id);
    Repo.states.set(ms.id, ms);

    json.districts.forEach(distJson => {
      let md = MasterDistrict.loadFromInitialJson(distJson, ms);
      ms.districts.push(md)
    })

    return ms
  }

  public toString() {
    return `<MasterDistrict: ${this.name}, ${this.id}, Districts:
    ${this.districts.length} of them
>`
  }
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
  }

  // TODO: TEMPORARY, eventually we'll either generate district geoms here,
  // or we'll get them from the server and so will have a json constructor
  // that populates them
  public setDistrictData(md: MasterDistrict) {
    this.districts4map.set(md.id, new DistrictForMap(this, md.id, md.data));
  }

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
  } else {
    throw Error("geometry is not polygonal: \n" + JSON.stringify(g));
  }
  return new L.Polygon(g.coordinates);
}
