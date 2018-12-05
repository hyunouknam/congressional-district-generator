import { UIDable, UIDRepository } from '../utils';


export type SimulationData = string;

//keep track of all simulataions



export class Simulation implements UIDable {
  public data: SimulationData | null;
  public owner: User|null;

  // persistence stuff
  static repo : UIDRepository<Simulation> = new UIDRepository();
  static createParse(data: any): Simulation {
    let testObj: {
      id: string;
      name: string;
      data: string | null;
    }

    testObj = data;
    return new Simulation(testObj.id, testObj.name, testObj.data);
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
      username: string;
      id: string;
      savedData: Array< {
          name: string;
          id: string;
          data: string|null;
                        } >
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

    let sims = data.savedData.map(s => Simulation.createParse(s));
    let user = new User(data.id, data.username, sims);

    return user; 
  }


  public readonly simulations: Simulation[];

  public constructor(public readonly id: string, public readonly name: string, sim: Simulation[] = []) {
    this.simulations = sim;
    User.repo.addItem(id, this);
  }



  public greet() { return "Hello " + this.name + "!"; }
}
