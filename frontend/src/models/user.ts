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


export class User implements UIDable {


  static repo: UIDRepository<User> = new UIDRepository();

  //TODO ! BUG WARNING: what happens if we error out in the middle of the thing? transactional?
  static createParse(data: string): User {
    //parse serialized user json into user obj

    let testObj: {
      username: string;
      id: string;
      savedData: Array< {
          name: string;
          id: string;
          data: string|null;
                        } >
    };

    testObj = JSON.parse(data);
    console.log("==========");
    console.log(testObj);

    //TODO: TYPECHECKS?
    //function assert(b: boolean) { if (!b) throw new SyntaxError(); }
    //assert(testObj.username == "string");
    //assert(testObj.id == "string");
    //assert(testObj.savedData) == "");

    let sims = testObj.savedData.map(s => Simulation.createParse(s));
    let user = new User(testObj.id, testObj.username, sims);

    return user; 
  }


  public readonly simulations: Simulation[];

  public constructor(public readonly id: string, public readonly name: string, sim: Simulation[] = []) {
    this.simulations = sim;
    User.repo.addItem(id, this);
  }



  public greet() { return "Hello " + this.name + "!"; }
}
