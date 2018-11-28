package Simulation;

import Areas.MasterState;
import Areas.PrecinctForMap;
import java.util.Set;

public class SimulationParams {
    private FunctionWeights functionWeights;
    private MasterState forState;
    private Set<PrecinctForMap> excludedPrecincts;
    
    public MasterState getState(){
        return forState;
    }
    
}
