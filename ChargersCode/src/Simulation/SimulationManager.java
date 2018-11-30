package Simulation;

import Users.UserAccount;
import java.util.Set;

public abstract class SimulationManager {
    private Set<Simulation> simulations;
    
    //creates simulation object, and adds simulation to simulationworker's queue
    public abstract Simulation createSim(UserAccount user, SimulationParams params);
    
    public Simulation[] getSimulationsForUser(UserAccount ua){
        //pull sims from database using entitymanager
        return null;
    }
}
