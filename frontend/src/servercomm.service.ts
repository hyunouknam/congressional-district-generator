import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

import { User, UserJSON, Simulation, SimulationJSON } from "./models/user"
import { SimParams } from "./models/params";
import { MasterStateJson } from "./models/geometry";

import { resolveAfter } from './utils';


//<id> to be replaced by id
const api_urls = {
    fetchUserData: '/api/getUserData',
    startSimulation: '/api/startSimulation',
    deleteSimulation: '/api/deletesim/<id>',
    reqInitialGeomData: '/api/fetchInitialStates',
    reqPrecinctData: '/api/fetchPrecinctsByDistrict/<id>',
    reqStateTopoJson: '/assets/<id>_topo.json',
}



// ============= DEBUGGING STUBS!!!
function pickRandom<T>(arr: T[]): T {
    let n = arr.length;
    let index = Math.floor(Math.random() * n);
    return arr[index];
}



let TEST_JSONS = [
`{
"username": "UserTest",
"id": "1",
"savedData": [
  {"name": "simA", "id":1, "data": null},
  {"name": "simB", "id":2, "data": null},
  {"name": "simC", "id":3, "data": null}
]
}`,

`{
"username": "UserSample",
"id": "2",
"savedData": [
  {"name": "simA", "id":4, "data": null},
  {"name": "otherSim", "id":5, "data": null}
]
}`,

]
// ============= END DEBUGGING STUBS


@Injectable({
  providedIn: 'root',
})
export class ServerCommService {


  constructor(private http: HttpClient) {
  }



  //TODO: maybe should return a User promise instead?
  public reqFetchUserData(): Promise<UserJSON> {
    //GET /api/getUserData
    return this.http.get<UserJSON>(api_urls.fetchUserData).toPromise();

  }


  public reqStartSimulation(params: SimParams): Promise<Simulation> {
    //POST /api/startSimulation
    //body: params.serialize()
    //response: serialized simulation, do Simulation.createParse(JSON);
    
    console.log("Doing startSimulation:" +  JSON.stringify(params, null, "  "));
    
    //get response
    let req = this.http.post<SimulationJSON>(api_urls.startSimulation, params).toPromise();
    
    //parse response
    return req.then(json => Simulation.createParse(json));
  }


  public reqDeleteSimulation(sim: Simulation): Promise<void> {
    //POST /api/deleteSimulation
    //body: sim.getFullId()
    
      console.log(`About to delete simulation ${sim.id}`);
      return this.http.get<void>(api_urls.deleteSimulation.replace('<id>', sim.id))
        .toPromise()
  }


  // ============= get data
  //
  public reqInitialGeomData(): Promise<MasterStateJson[]> {
      //POST /api/fetchInitialGeoms
      return this.http.get<MasterStateJson[]>(
        api_urls.reqInitialGeomData)
      .toPromise();
      //return Promise.resolve(dummyData);
  }

  // //=======TODO DELETE
  //public reqPrecinctsForDistrict(districtId: string) {
  //  return this.http.get<MasterPrecinctJson[]>(
  //    api_urls.reqPrecinctData.replace('<id>', districtId))
  //  .toPromise();
  //}


  public reqStateTopoJson(stateId: string) {
    if(["CT","NJ","NE"].indexOf(stateId) < 0) {
        throw Error(`INVALID STATE ID '${stateId}'`);
    }
    return this.http.get<any>(
      api_urls.reqStateTopoJson.replace('<id>', stateId))
    .toPromise();
  }


}



