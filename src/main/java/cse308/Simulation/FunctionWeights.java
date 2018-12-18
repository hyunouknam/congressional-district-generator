package cse308.Simulation;

import org.json.JSONObject;

public class FunctionWeights {
    final double w_compactness;
    final double w_population_equality;
    final double w_partisan_fairness;
    
    public FunctionWeights(double d, double e, double f){
        w_compactness=d;
        w_population_equality=e;
        w_partisan_fairness=f;
    }
    
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("wCompactness", w_compactness);
        json.put("wPopulationEquality", w_population_equality);
        json.put("wPartisanFairness", w_partisan_fairness);
        return json;
    }
}
