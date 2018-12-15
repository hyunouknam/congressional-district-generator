package cse308.Simulation;

import java.util.ArrayList;
import java.util.List;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ObjectiveFuncEvaluator {


    public static double calcCompactness(Map m){
        double total = 0;
        
        List<Double> polsbyScoreList = new ArrayList<>();
        for (DistrictForMap d: m.getAllDistricts()){        	
        	double polsbyScore = (4 * Math.PI * d.getArea()) / Math.pow(d.getPerimeter(), 2);
        	polsbyScoreList.add(polsbyScore);	// added to list in case we want more than average
        	total += polsbyScore;
        }
        
        double average = total / m.getState().getNumOfDistricts();
        return average;
        //throw new NotImplementedException();
        //return total / m.getAllDistricts().size();
    }

    public static double calcPopulationEquality(Map m){
        double total = 0;
        double avgPop = 1; // ...calcAvgPop()

        for (DistrictForMap d: m.getAllDistricts()){
        	
        	
        	
        	
            //double distance = 1; // d.getPop() - avgPop  // Or something. TODO
            //total += distance; //TODO, we might not want just an average
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
