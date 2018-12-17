package cse308.Simulation;

import cse308.Areas.Map;
import cse308.Areas.MasterState;


public class SimulatedAnnealingParams extends SimulationParams{
    Map startingMap;
    public SimulatedAnnealingParams(FunctionWeights weights, MasterState state, String algorithm, Map startingMap){
        super(weights, state, algorithm);
        this.startingMap=startingMap;
    }
}