package cse308.Simulation;

import java.util.Set;

import cse308.Areas.MasterState;
import cse308.Areas.PrecinctForMap;

public class SimulationParams {
    final FunctionWeights functionWeights;
    final MasterState forState;
    final String algorithm;
//    Set<PrecinctForMap> excludedPrecincts;
    private SimulationManager simManager;
    
    public SimulationParams(FunctionWeights weights, String state, String algorithm){
    	SimulationManager simManager = SimulationManager.getInstance();
    	
    	functionWeights=weights;
    	forState=simManager.getState(state);
        this.algorithm=algorithm;
    }
    
    public String getJSON() {
    	String s = "{";
		s = s + "\"functionWeights\":";
		s = s + functionWeights.getJSON();
		s = s + ", \"forState\":";
		s = s + "\"" + "NJ" + "\"";
		s = s + ", \"algorithm\":";
		s = s + "\"" + algorithm + "\"";
		s = s + "}";
		return s;
    }
    
//    public void addExcusion(PrecinctForMap p){
//        excludedPrecincts.add(p);
//    }
    
    
    
}
