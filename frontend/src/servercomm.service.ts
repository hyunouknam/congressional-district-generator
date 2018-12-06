import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

import { User, UserSerializedJSON, Simulation, SimulationSerializedJSON} from "./models/user"
import { SimParams } from "./models/params";

import { resolveAfter } from './utils';


const api_urls = {
    fetchUserData: '/api/getUserData',
    startSimulation: '/api/startSimulation',
    deleteSimulation: '/api/startSimulation/<id>',
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
  // ========== USER DATA
  // Interface is weird because we want to load it immediately
  // In some cases we want to wait for it (promise), other times use it immediately
  //TODO make this clean with observables maybe?
  currentUser: User|null = null;  
  userPromise: Promise<User|null>;

  public getCurrentUserPromise() { return this.userPromise;}
  public getCurrentUser() { return this.currentUser; }


  constructor(private http: HttpClient) {
    this.userPromise = this.reqFetchUserData()       // Real query
    //this.userPromise = resolveAfter(pickRandom(TEST_JSONS), 500)  // TODO DEBUG: stub
        .then(json => User.createParse(json));

    //Do the catch after we assign.
    //It's a little funky. Maybe switch to observables might be good?
    this.userPromise
      .then(user => this.currentUser = user)
      .catch(err => {console.error(err); console.log("FAILED TO GET USER"); });
  }



  //TODO: maybe should return a User promise instead?
  public reqFetchUserData(): Promise<UserSerializedJSON> {
    //GET /api/getUserData
    return this.http.get<UserSerializedJSON>(api_urls.fetchUserData).toPromise();

  }


  public reqStartSimulation(params: SimParams): Promise<Simulation> {
    //POST /api/startSimulation
    //body: params.serialize()
    //response: serialized simulation, do Simulation.createParse(JSON);
    
    console.log("Doing startSimulation:" +  JSON.stringify(params, null, "  "));
    
    //get response
    let req = this.http.post<SimulationSerializedJSON>(api_urls.startSimulation, params).toPromise();
    
    //parse response
    return req.then(json => Simulation.createParse(json));
  }


  public reqDeleteSimulation(sim: Simulation): Promise<void> {
    //POST /api/deleteSimulation
    //body: sim.getFullId()
    
    return Promise.reject("REQUEST NOT IMPLEMENTED");
  }


}



