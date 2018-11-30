import { Injectable } from "@angular/core";

import { User, Simulation } from "./models/user"
import { SimParams } from "./models/params";

import { resolveAfter } from './utils';

import { UsersService } from './users.service';


@Injectable({
  providedIn: 'root',
})
export class ServerCommService {

  constructor(users: UsersService) {
  }

  //TODO: maybe should return a User promise instead?
  public reqFetchUserData(): Promise<string> {
    // DEBUG: STUBBED
    return Promise.reject("REQUEST NOT IMPLEMENTED");
  }


  public reqStartSimulation(params: SimParams): Promise<Simulation> {
    // get user 
    console.log("Doing startSimulation:" +  JSON.stringify(SimParams, null, "  "));
    return Promise.reject("REQUEST NOT IMPLEMENTED");
  }


  public reqDeleteSimulation(sim: Simulation): Promise<void> {
    return Promise.reject("REQUEST NOT IMPLEMENTED");
  }


}



