package Simulation;

import Areas.Map;

public class ObjectiveFuncEvaluator {
    public int calcCompactness(Map m){
        return 1;
    }
    public int calcPopulationEquality(Map m){
        return 0;
    }
    public int calcPartisanFairness(Map m){
        return 0;
    }
    public int calcRacialFairness(Map m){
        return 0;
    }
    public int calcContiguity(Map m){
        return 0;
    }
    
    
    public float evaluateObjective(FunctionWeights w, Map m){
        return 0;
    }
}
