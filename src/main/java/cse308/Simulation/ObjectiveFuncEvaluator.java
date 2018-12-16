package cse308.Simulation;

import java.util.ArrayList;
import java.util.List;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.*;

public class ObjectiveFuncEvaluator {

    private static final double CORRECTION_FACTOR = 0.5708;
    
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
    }

    public static double calcPopulationEquality(Map m){
        double total = 0;
        double avgPop = 1; // ...calcAvgPop()

        List<DistrictForMap> districts = new ArrayList<>();
    	for (DistrictForMap d: m.getAllDistricts()){
        	districts.add(d);
        }
        
        double[] districtPopulations = districts
        		.stream()
        		.mapToDouble(x -> x.getPopulation())
        		.sorted()
        		.toArray();
        
        double popAvg = StatUtils.mean(districtPopulations);
        double popVariance = StatUtils.variance(districtPopulations);
        double popDeviation = Math.sqrt(popVariance);
        
        double score = popDeviation / popAvg;
        
        return (1.0 - score);
        		
        //return total;
    }
    
    public static double calcPartisanFairness(Map m){
        //Use Consistent Advantage
        
    	List<DistrictForMap> districts = new ArrayList<>();
    	for (DistrictForMap d: m.getAllDistricts()){
        	districts.add(d);
        }
        double[] democratVoteShares = districts
        		.stream()
        		.mapToDouble(x -> x.getPercentDemocrat())
        		.sorted()
        		.toArray();

        double democratAvgShare = StatUtils.mean(democratVoteShares);
        double democratMedShare = StatUtils.percentile(democratVoteShares, 50);
        double democratShareVar = StatUtils.variance(democratVoteShares); 
        
        double meanMedDiff = democratAvgShare - democratMedShare;
        double standardError = Math.sqrt(democratShareVar ) / Math.sqrt(democratVoteShares.length);
        double zScore = meanMedDiff / standardError;
        zScore = (zScore * CORRECTION_FACTOR);
        
        double pValue = new NormalDistribution(0, 1)
                .cumulativeProbability(zScore);
        
        return (pValue / 10.0);
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
