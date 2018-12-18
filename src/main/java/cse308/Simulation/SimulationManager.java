package cse308.Simulation;

import cse308.Users.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class SimulationManager {

    @Autowired
    private SavedSimRepository simRepo;

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

        if(params.algorithm.contains("REGION_GROWING")){
            newSim=new RegionGrowingSimulation((RegionGrowingParams)params);
        }
        else{
            newSim=new SimulatedAnnealingSimulation((SimulatedAnnealingParams)params);
        }

        SavedSimulation saveSim = new SavedSimulation(user, params, newSim.currentMap);
        newSim.setSavedSim(saveSim);
        simRepo.save(saveSim);
        user.getMaps().add(saveSim);


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
	
	public void deleleSim(int id) {
		System.out.println("In sim manager");
		simRepo.deleteById(id);
	}
}