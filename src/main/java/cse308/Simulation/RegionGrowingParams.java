package cse308.Simulation;

import cse308.Areas.MasterState;
import java.util.Set;

public class RegionGrowingParams extends SimulationParams{
    final Set<Integer> seedPrecincts;//IDs of the precincts chosen as a seed
    
    public RegionGrowingParams(FunctionWeights weights, MasterState state, String algorithm, int districts, Set<Integer> seeds){
        super(weights, state, algorithm, districts);
        seedPrecincts=seeds;
        numDistricts=districts;
    }
}