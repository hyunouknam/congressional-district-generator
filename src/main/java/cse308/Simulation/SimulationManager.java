package cse308.Simulation;

import cse308.Areas.MasterState;
import cse308.Users.UserAccount;

import java.util.HashSet;
import java.util.Set;

public class SimulationManager {
	private static SimulationManager simManager = null; 
	//can hold all master states? 
    static Set<MasterState> states;    
    private static Set<Simulation> simulations;
    
    public SimulationManager() {
    	MasterState nj = new MasterState("NJ", "Constitution", true, 13);
    	MasterState ct = new MasterState("CT", "Constitution", true, 5);
    	
    	states = new HashSet<>();
    	states.add(nj);
    	states.add(ct);
    }
    
    public static SimulationManager getInstance() 
    { 
        if (simManager == null) 
            simManager = new SimulationManager(); 
  
        return simManager; 
    } 
    
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
    
    public MasterState getState(String stateName){
        for(MasterState state: states){
            if(state.getName().equals(stateName)){
                return state;
            }
        }
        return null;
    }
}
