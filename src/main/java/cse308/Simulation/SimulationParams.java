package cse308.Simulation;

import java.util.Set;

import cse308.Areas.MasterState;
import cse308.Areas.PrecinctForMap;

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
