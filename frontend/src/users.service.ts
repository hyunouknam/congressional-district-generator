import { Injectable } from "@angular/core";
import { ServerCommService } from "./servercomm.service";
import { MapHandlerService } from "./maphandler.service";
import { User, UserJSON, Simulation, SimulationJSON } from "./models/user";

import { dummyUser } from './dummy_data';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  public readonly user_p: Promise<User> ;
  public readonly sims_p: Promise<Simulation[]>;

  constructor(private servercomm: ServerCommService, private maphandler: MapHandlerService) {
      (window as any).U = this; //TODO TEMP DEBUG

      //const userdata: Promise<UserJSON> = this.servercomm.reqFetchUserData()
      const userdata: Promise<UserJSON> = Promise.resolve(dummyUser);


      this.user_p = userdata.then(json => User.createParse(json));

      //For sims, we have to wait until map data is loaded 
      this.sims_p = Promise.all([userdata, maphandler.dataLoad]).then(vals => {
        const [userjson, _] = vals;
        return userjson.simulations.map(simjson => Simulation.createParse(simjson));
      })

  }


}
