import { Injectable } from "@angular/core";
import { User, Simulation } from "./models/user"


function pickRandom<T>(arr: T[]): T {
    let n = arr.length;
    let index = Math.floor(Math.random() * n);
    return arr[index];
}




const TEST_USERS = ["UserTest", "SampleUser", "UserX"].map(name => new User(name));

TEST_USERS[0].simulations = [
  new Simulation("sim1", "12"),
  new Simulation("sim2", "13"),
  new Simulation("sim3", "5"),
]

TEST_USERS[1].simulations = []
TEST_USERS[2].simulations = null;



@Injectable({
  providedIn: 'root',
})
export class UsersService {

  // on startup, pick one to be the current users
  currentUser: Promise<User|null>;

  constructor() {
    this.currentUser = Promise.resolve(pickRandom(TEST_USERS));
  }

  public getUser() { 
    return this.currentUser;
  }

}


