package Simulation;

import Areas.MasterState;
import Areas.PrecinctForMap;
import java.util.Set;

public class SimulationParams {
    final FunctionWeights functionWeights;
    final MasterState forState;
    final String algorithm;
    Set<PrecinctForMap> excludedPrecincts;
    
    public SimulationParams(FunctionWeights weights, String state, String algorithm){
        forState=SimulationManager.getState(state);
        this.algorithm=algorithm;
        functionWeights=weights;
    }
    
    public void addExcusion(PrecinctForMap p){
        excludedPrecincts.add(p);
    }
    
    
    
}
