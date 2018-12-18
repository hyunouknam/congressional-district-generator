package cse308.Simulation;

import cse308.Users.UserAccount;

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
    
    /*
    Description:
        creates simulation object, and adds simulation to simulationworker's queue
    */
    public Simulation createSim(UserAccount user, SimulationParams params){
        Simulation newSim;
        if(params.algorithm.equals("REGION_GROWING")){
            newSim=new RegionGrowingSimulation(user, (RegionGrowingParams)params);
        }
        else{
            newSim=new SimulatedAnnealingSimulation(user, (SimulatedAnnealingParams)params);
        }
        simWorker.addToRunQueue(newSim);
	simWorker.runNextSimulation();
        return newSim;
    }
    
    //Continues a saved simulation from where it left off
    public void continueSimulation(){
        Simulation sim=null;//=EntityManager.findSimulation(simID);
        simWorker.addToRunQueue(sim);
    }
    
    public Simulation[] getSimulationsForUser(UserAccount ua){
        //EntityManger.findSimulations(String username);
        return null;
    }

	public SimulationWorker getSimWorker() {
		return simWorker;
	}
}