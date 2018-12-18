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
    
    public String getJSON() {
    	String s = "{";
		s = s + "\"w_compactness\":";
		s = s + "\"" + w_compactness + "\"";
		s = s + ", \"w_population_equality\":";
		s = s + "\"" + w_population_equality + "\"";
		s = s + ", \"w_partisan_fairness\":";
		s = s + "\"" + w_partisan_fairness + "\"";
		s = s + "}";
		return s;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("w_compactness", w_compactness);
        json.put("w_population_equality", w_population_equality);
        json.put("w_partisan_fairness", w_partisan_fairness);
        return json;
    }
}
