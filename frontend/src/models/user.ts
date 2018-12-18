import { Repo, StateMapJson, StateMap } from './geometry';
import { SimParamsJson} from './params';

export type SimulationData = string;

//keep track of all simulataions

export type SimulationJSON = {
    id: string;
    params: SimParamsJson;
    data: StateMapJson[];
}


export class Simulation {
  public constructor(public id: string, public params: SimParamsJson, public data: StateMap[]) {
  }

  // persistence stuff
  static createParse(json: SimulationJSON): Simulation {
    
    const stateId = json.params.state;
    const state = Repo.states.get(stateId);
    if(!state) { throw Error("Invalid State " + stateId); }

    const maps: StateMap[] = [];
    for(const mapjson of json.data) {
      maps.push(StateMap.loadFromJson(mapjson, state));
    }

    return new Simulation(json.id, json.params, maps);
  }

  public toString() {
    return `<Simulation #${this.id}, params: ${JSON.stringify(this.params)}, map for ${this.data[0].state.id}>`;
  }

}




// ==========================================================
//                  USER STUFF


export type UserJSON = {
      id: string;
      username: string;
      simulations: SimulationJSON[];
}

export class User {

  //TODO ! BUG WARNING: what happens if we error out in the middle of the thing? transactional?
  static createParse(data: UserJSON): User {
    //parse serialized user json into user obj

    console.log("==========");
    console.log(data);

    //TODO: TYPECHECKS?
    //function assert(b: boolean) { if (!b) throw new SyntaxError(); }
    //assert(testObj.username == "string");
    //assert(testObj.id == "string");
    //assert(testObj.savedData) == "");

    //let sims = data.simulations.map(s => Simulation.createParse(s));
    let user = new User(data.id, data.username);

    return user; 
  }


  public readonly simulations: Simulation[];

  public constructor(public readonly id: string, public readonly username: string) {
  }



  public greet() { return "Hello " + this.username + "!"; }
}
