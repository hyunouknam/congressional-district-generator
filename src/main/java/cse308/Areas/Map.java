package cse308.Areas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cse308.Simulation.FunctionWeights;
import cse308.Simulation.Move;
import cse308.Simulation.ObjectiveFuncEvaluator;

//@Entity
public class Map implements Cloneable{
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	
//	@ManyToOne
//	@JoinColumn(name="state_id")
    private final MasterState master;

    // Mapping objects, not managed by Spring Hibernate
    private HashMap<MasterPrecinct, PrecinctForMap> precincts;
    private HashMap<MasterDistrict, DistrictForMap> districts;
    private DistrictForMap nullDistrict;
    private HashMap<PrecinctForMap, DistrictForMap> precinctDistrictMapping;

    private double currentGoodness;


    public Map(MasterState state){
        master = state;

        //prepare precincts, associate with master precincts
        precincts = new HashMap<>();
        for(MasterPrecinct mp: master.getPrecincts()){
            PrecinctForMap p = new PrecinctForMap(mp);
            precincts.put(mp, p);
        }

        //prepare districts
        districts = new HashMap<>();
        nullDistrict=new DistrictForMap();
        for(MasterDistrict md: master.getDistricts()){
            DistrictForMap d = new DistrictForMap(md);
            districts.put(md, d);
        }

        // Initialize, all precincts start in null district
        this.precinctDistrictMapping = new HashMap<>();
        for(PrecinctForMap p: getAllPrecincts()) {
            precinctDistrictMapping.put(p, this.nullDistrict);
        }
    }

	// ======= GETTERS

    public MasterState getState(){ return master; }
	public Collection<PrecinctForMap> getAllPrecincts(){ return precincts.values(); }
    public Collection<DistrictForMap> getAllDistricts(){ return districts.values(); }
    public DistrictForMap getNullDisrict(){ return nullDistrict; }

    public HashMap<PrecinctForMap, DistrictForMap> getPrecinctDistrictMapping(){ return precinctDistrictMapping; }

    public double getGoodness(){ return currentGoodness; }

    public PrecinctForMap getPrecinct(MasterPrecinct precinct){ return precincts.get(precinct); }
    public DistrictForMap getDistrict(MasterDistrict district){ return districts.get(district); }


    // ========== Rest of it

    public void apply(FunctionWeights weights, Move m){
        calculateGoodness(weights);
        precinctDistrictMapping.put(m.getPrecinct(), m.getNewDistrict()); //modifies precint to district mapping        
    }
    
    public Map cloneApply(FunctionWeights weights, Move m) {
        Map newMap = clone();
        newMap.calculateGoodness(weights);
        newMap.getPrecinctDistrictMapping().put(m.getPrecinct(), m.getNewDistrict());
        return newMap;
    }
    
    public double calculateGoodness(FunctionWeights weights){
        currentGoodness=ObjectiveFuncEvaluator.evaluateObjective(weights,this);
        return currentGoodness;
    }
    


    @Override
    public Map clone() {
        Map copy = new Map(master);

        //Make sure all precincts are assigned to their corresponding districts
        for(PrecinctForMap p: getAllPrecincts()){
            //get corresponding precinct in copy
            MasterPrecinct mp = p.getMaster();
            PrecinctForMap p_copy = copy.getPrecinct(mp);

            //get corresponding parent district in copy
            MasterDistrict md = p.getParentDistrict().getMaster();
            DistrictForMap d_copy = copy.getDistrict(md);

            //assign copy's version of p to copy's version of d
            copy.precinctDistrictMapping.put(p_copy, d_copy);
        }

        return copy;
    }
}