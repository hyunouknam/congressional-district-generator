package Simulation;

import Areas.MasterState;
import Users.UserAccount;
import java.util.Set;

public abstract class SimulationManager {
   //can hold all master states? 
    static Set<MasterState> states;    
    private static Set<Simulation> simulations;
    
    /*
    Description:
        creates simulation object, and adds simulation to simulationworker's queue
    */
    public static Simulation createSim(UserAccount user, SimulationParams params){
        Simulation newSim;
        if(params.algorithm.equals("Region Growing")){
            newSim=new RegionGrowingSimulation(user, params);
        }
        else{
            newSim=new SimulatedAnnealingSimulation(user, params);
        }
        SimulationWorker.addToRunQueue(newSim);
        return newSim;
    }
    
    public Simulation[] getSimulationsForUser(UserAccount ua){
        //EntityManger.findSimulations(String username);
        return null;
    }
    
    public static MasterState getState(String stateName){
        for(MasterState state: states){
            if(state.getName().equals(stateName)){
                return state;
            }
        }
        return null;
    }
}
