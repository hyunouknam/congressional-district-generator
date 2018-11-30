package Simulation;

public class FunctionWeights {
    final float w_compactness;
    final float w_population_equality;
    final float w_partisan_fairness;
    
    public FunctionWeights(float comp, float pop, float fair){
        w_compactness=comp;
        w_population_equality=pop;
        w_partisan_fairness=fair;
    }
}
