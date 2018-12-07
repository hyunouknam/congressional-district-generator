package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;

public class ObjectiveFuncEvaluator {
    public static double calcCompactness(DistrictForMap m){
        return 1;
    }
    public static double calcPopulationEquality(DistrictForMap m){        
        return 0;
    }
    public static double calcPartisanFairness(DistrictForMap m){
        return 0;
    }
    public static double calcRacialFairness(DistrictForMap m){
        return 0;
    }
    public static double calcContiguity(DistrictForMap m){
        return 0;
    }    
    
    public static double evaluateObjective(FunctionWeights w, DistrictForMap m){
       return w.w_compactness*calcCompactness(m)+w.w_partisan_fairness*calcPartisanFairness(m)+w.w_population_equality*calcPopulationEquality(m);
    }
}
