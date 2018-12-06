package cse308.Simulation;

import cse308.Areas.Map;

public class ObjectiveFuncEvaluator {
    public static double calcCompactness(Map m){
        return 1;
    }
    public static double calcPopulationEquality(Map m){        
        return 0;
    }
    public static double calcPartisanFairness(Map m){
        return 0;
    }
    public static double calcRacialFairness(Map m){
        return 0;
    }
    public static double calcContiguity(Map m){
        return 0;
    }    
    
    public static double evaluateObjective(FunctionWeights w, Map m){
       return w.w_compactness*calcCompactness(m)+w.w_partisan_fairness*calcPartisanFairness(m)+w.w_population_equality*calcPopulationEquality(m);
    }
}
