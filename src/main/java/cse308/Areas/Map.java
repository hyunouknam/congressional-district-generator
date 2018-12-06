package cse308.Areas;

import java.util.HashMap;

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
public class Map{
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	
//	@ManyToOne
//	@JoinColumn(name="state_id")
    private final MasterState master;
    private HashMap<MasterPrecinct, PrecinctForMap> precincts;
    private HashMap<MasterDistrict, DistrictForMap> districts;
    private DistrictForMap nullDistrict;
    private HashMap<PrecinctForMap, DistrictForMap> precinctDistrictMapping;
    private float currentGoodness;
    
//    public Map(MasterState state){
//        //should id be static or changed by DB?
//        master=state;
//        nullDistrict=new DistrictForMap();
//        for(MasterPrecinct mp: master.getPrecincts()){
//            PrecinctForMap pm=new PrecinctForMap(mp);
//            precincts.put(mp, pm);
//            nullDistrict.getPrecincts().add(pm);
//        }
//        for(MasterDistrict md: master.getDistricts()){
//            DistrictForMap dm=new DistrictForMap(md);
//            districts.put(md, dm);
//        }
//    }
    
    public Map(MasterState state) {
		// TODO Auto-generated constructor stub
    	master=state;
	}

	public HashMap<MasterPrecinct, PrecinctForMap> getAllPrecincts(){
        return precincts; //or return just the precinctformaps?
    }
    
    public HashMap<MasterDistrict, DistrictForMap> getAllDistricts(){
        return districts;
    }
    
    public PrecinctForMap getPrecinct(MasterPrecinct precinct){
        return precincts.get(precinct); 
    }
    
    public DistrictForMap getDistrict(MasterDistrict district){
        return districts.get(district);
    }
    
    public void apply(FunctionWeights weights, Move m){
        calculateGoodness(weights);
        precinctDistrictMapping.put(m.getPrecinct(), m.getNewDistrict()); //modifies precint to district mapping        
    }
    
    public Map cloneApply(FunctionWeights weights, Move m) throws CloneNotSupportedException{
        Map newMap=(Map)super.clone();
        newMap.calculateGoodness(weights);
        newMap.getPrecinctDistrictMapping().put(m.getPrecinct(), m.getNewDistrict());
        return newMap;
    }
    
    public MasterState getState(){
        return master;
    }
    
    public DistrictForMap getNullDisrict(){
        return nullDistrict;
    }
    
    public float getGoodness(){
        return currentGoodness;
    }
    
    public void calculateGoodness(FunctionWeights weights){
        ObjectiveFuncEvaluator.evaluateObjective(weights,this);
    }
    
    public MasterState getMaster(){
        return master;
    }
    
    public HashMap<PrecinctForMap, DistrictForMap> getPrecinctDistrictMapping(){
        return precinctDistrictMapping;
    }
}