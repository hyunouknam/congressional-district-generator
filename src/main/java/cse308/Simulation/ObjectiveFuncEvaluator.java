package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ObjectiveFuncEvaluator {


    public static double calcCompactness(Map m){
        double total = 0;
        for (DistrictForMap d: m.getAllDistricts()){
            double distCompactness = 1; // TODO actually implement this
            total += distCompactness; //TODO, we might not want just an average
        }

        throw new NotImplementedException();
        //return total / m.getAllDistricts().size();
    }

    public static double calcPopulationEquality(Map m){
        double total = 0;
        double avgPop = 1; // ...calcAvgPop()

        for (DistrictForMap d: m.getAllDistricts()){
            double distance = 1; // d.getPop() - avgPop  // Or something. TODO
            total += distance; //TODO, we might not want just an average
            //Maybe we want variance
        }

        throw new NotImplementedException();
        //return total;
    }
    public static double calcPartisanFairness(Map m){
        throw new NotImplementedException();
    }
    public static double calcRacialFairness(Map m){
        throw new NotImplementedException();
    }
    public static double calcContiguity(Map m){
        throw new NotImplementedException();
    }

    public static double evaluateObjective(FunctionWeights w, Map m){
       return (  w.w_compactness            * calcCompactness(m)
               + w.w_partisan_fairness      * calcPartisanFairness(m)
               + w.w_population_equality    * calcPopulationEquality(m)
       );
    }
}
