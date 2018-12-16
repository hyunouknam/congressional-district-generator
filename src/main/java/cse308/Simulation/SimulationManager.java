package cse308.Simulation;

import cse308.Areas.MasterState;
import cse308.Users.UserAccount;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimulationManager {
    private static SimulationManager simManager = null;
    private SimulationWorker simWorker = null;
    
    public SimulationManager() {
        simWorker = new SimulationWorker();
    }
    
    public static SimulationManager getInstance() 
    { 
        if (simManager == null) 
            simManager = new SimulationManager(); 
        return simManager; 
    } 
    
//    public static SimulationWorker getSimWorker(){
//        if(simWorker==null){
//            simWorker=new SimulationWorker();
//            simWorker.run();
//        }
//        return simWorker;
//    }
    
    /*
    Description:
        creates simulation object, and adds simulation to simulationworker's queue
    */
    public Simulation createSim(UserAccount user, SimulationParams params){
        Simulation newSim;
        if(params.algorithm.equals("Region Growing")){
            newSim=new RegionGrowingSimulation(user, params);
        }
        else{
            newSim=new SimulatedAnnealingSimulation(user, params);
        }
        simWorker.addToRunQueue(newSim);
        return newSim;
    }
    
    public Simulation[] getSimulationsForUser(UserAccount ua){
        //EntityManger.findSimulations(String username);
        return null;
    }

	public SimulationWorker getSimWorker() {
		return simWorker;
	}
}