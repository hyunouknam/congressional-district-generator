import { UIDable, UIDRepository } from '../utils';


export type SimulationData = string;

//keep track of all simulataions

export type SimulationSerializedJSON = {
    name: string;
    id: string;
    params: any;
    data: string | null;
}



export class Simulation implements UIDable {
  public data: SimulationData | null;
  public owner: User|null;

  // persistence stuff
  static repo : UIDRepository<Simulation> = new UIDRepository();
  static createParse(json: SimulationSerializedJSON): Simulation {
    return new Simulation(json.id, json.name, json.data);
  }

  public constructor(public id: string, public name: string, data?: SimulationData|null) {
      this.data = data || null;
      Simulation.repo.addItem(id, this);
  }



  public getFullId() {
    return "Simulation#" + this.id;
  }

}





// ==========================================================
//                  USER STUFF


export type UserSerializedJSON = {
      id: string;
      user: string;
      simulations: SimulationSerializedJSON[];
}

export class User implements UIDable {


  static repo: UIDRepository<User> = new UIDRepository();

  //TODO ! BUG WARNING: what happens if we error out in the middle of the thing? transactional?
  static createParse(data: UserSerializedJSON): User {
    //parse serialized user json into user obj

    console.log("==========");
    console.log(data);

    //TODO: TYPECHECKS?
    //function assert(b: boolean) { if (!b) throw new SyntaxError(); }
    //assert(testObj.username == "string");
    //assert(testObj.id == "string");
    //assert(testObj.savedData) == "");

    let sims = data.simulations.map(s => Simulation.createParse(s));
    let user = new User(data.id, data.user, sims);

    return user; 
  }


  public readonly simulations: Simulation[];

  public constructor(public readonly id: string, public readonly name: string, sim: Simulation[] = []) {
    this.simulations = sim;
    User.repo.addItem(id, this);
  }



  public greet() { return "Hello " + this.name + "!"; }
}
