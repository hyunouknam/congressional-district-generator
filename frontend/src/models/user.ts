
export class Simulation {
  constructor(public name: string, public data: string) {
  }
}


export class User {

  simulations: Simulation[] | null; // null while not loaded

  constructor(public readonly name: string){}

  public greet() { return "Hello " + this.name + "!"; }
}
