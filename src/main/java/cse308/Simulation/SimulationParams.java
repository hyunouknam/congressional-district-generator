package cse308.Simulation;

import cse308.Areas.MasterState;

public class SimulationParams {
    final FunctionWeights functionWeights;
    final MasterState forState;
    final String algorithm;
    int numDistricts;
//    Set<PrecinctForMap> excludedPrecincts;
    
    public SimulationParams(FunctionWeights weights, MasterState state, String algorithm, int numDistricts){
    	assert weights 		!= null: "Sim weights must not be null";
    	assert state 		!= null: "Sim state must not be null";
    	assert algorithm 	!= null: "Sim algorithm must not be null";

    	SimulationManager simManager = SimulationManager.getInstance();
    	
    	functionWeights=weights;
    	forState=state;
        this.algorithm=algorithm;
        this.numDistricts=numDistricts;
    }
    
    public MasterState getState() {return forState; }
    
    public String getJSON() {
    	String s = "{";
		s = s + "\"functionWeights\":";
		s = s + functionWeights.getJSON();
		s = s + ", \"forState\":";
		s = s + "\"" + "NJ" + "\"";
		s = s + ", \"algorithm\":";
		s = s + "\"" + algorithm + "\"";
                s = s + ", \"numDistricts\":";
		s = s + "\"" + numDistricts + "\"";
		s = s + "}";
		return s;
    }
    
//    public void addExcusion(PrecinctForMap p){
//        excludedPrecincts.add(p);
//    } 
    
}