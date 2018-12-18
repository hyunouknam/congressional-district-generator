package cse308.Simulation;

import cse308.Areas.MasterState;
import org.json.JSONObject;

public class SimulationParams {
    final FunctionWeights functionWeights;
    private final MasterState forState;
    final String algorithm;
//    Set<PrecinctForMap> excludedPrecincts;
    
    public SimulationParams(FunctionWeights weights, MasterState state, String algorithm){
    	assert weights 		!= null: "Sim weights must not be null";
    	assert state 		!= null: "Sim state must not be null";
    	assert algorithm 	!= null: "Sim algorithm must not be null";
    	
    	functionWeights=weights;
    	forState=state;
        this.algorithm=algorithm;
    }
    
    public MasterState getState() {return forState; }
    
	public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("state", forState.getID());
		json.put("algorithm", algorithm);
		json.put("functionWeights", functionWeights.toJSON());
		return json;
	}


//    public void addExcusion(PrecinctForMap p){
//        excludedPrecincts.add(p);
//    } 
    
}