package cse308.Areas;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private HashMap<DistrictForMap, Set<PrecinctForMap>> districtPrecinctMapping;

    public Map(MasterState state){
        master = state;

        //prepare precincts, associate with master precincts
        precincts = new HashMap<>();
        for(MasterPrecinct mp: master.getPrecincts()){
            PrecinctForMap p = new PrecinctForMap(mp, this);
            precincts.put(mp, p);
        }

        //prepare districts
        districts = new HashMap<>();
        nullDistrict=new DistrictForMap(this);
        for(MasterDistrict md: master.getDistricts()){
            DistrictForMap d = new DistrictForMap(md, this);
            districts.put(md, d);
        }

        // Initialize, all precincts start in null district
        this.precinctDistrictMapping = new HashMap<>();
        for(PrecinctForMap p: getAllPrecincts()) {
            precinctDistrictMapping.put(p, this.nullDistrict);
        }
        this.districtPrecinctMapping = new HashMap<>();
        districtPrecinctMapping.put(nullDistrict, new HashSet<>(getAllPrecincts()));
        for(DistrictForMap d: getAllDistricts()) {
        	districtPrecinctMapping.put(d, new HashSet<>());
        }
    }

	// ======= GETTERS0

    public MasterState getState(){ return master; }
    public Collection<PrecinctForMap> getAllPrecincts(){ return precincts.values(); }
    public Collection<DistrictForMap> getAllDistricts(){ return districts.values(); }
    public DistrictForMap getNullDisrict(){ return nullDistrict; }

    public HashMap<PrecinctForMap, DistrictForMap> getPrecinctDistrictMapping(){ return precinctDistrictMapping; }
    public HashMap<DistrictForMap, Set<PrecinctForMap>> getDistrictPrecinctMapping(){ return districtPrecinctMapping; }

	public PrecinctForMap getPrecinct(MasterPrecinct precinct){ return precincts.get(precinct); }
    public DistrictForMap getDistrict(MasterDistrict district){ return districts.get(district); }


    // ========== Rest of it

    public void apply(Move m){
        precinctDistrictMapping.put(m.getPrecinct(), m.getNewDistrict()); //modifies precint to district mapping
        DistrictForMap oldDistrict = m.getPrecinct().getParentDistrict();
        districtPrecinctMapping.get(oldDistrict).remove(m.getPrecinct());
        districtPrecinctMapping.get(m.getNewDistrict()).add(m.getPrecinct());
        //and calculate mas goofness
    }
    
    public Map cloneApply(Move m) {
        Map newMap = clone();
        newMap.getPrecinctDistrictMapping().put(m.getPrecinct(), m.getNewDistrict());
        //and calculate mas goofness
        return newMap;
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
    
    public String serializeMap() {
    	JSONObject c = new JSONObject();
    	System.out.println("Here in Sear");
    	for(DistrictForMap district: getAllDistricts()) {
    		Set<PrecinctForMap> precinctsForDistrict = districtPrecinctMapping.get(district);
    		Set<String> idsOfPrecincts = precinctsForDistrict.stream().map(p -> p.getMaster().getId()).collect(Collectors.toSet());
    		JSONArray arrayOfPrecincts = new JSONArray(idsOfPrecincts);
    		c.put(district.getMaster().getID(), arrayOfPrecincts);
    		System.out.println(district.getMaster().getID());
    	}
    	return c.toString();
    }
    
    public String toString() {
    	String s = "State Name: " + master.getName() + "\n";
    	s = s + districts.keySet().stream().map(md -> md.getID()).collect(Collectors.toList()).toString();
    	s =s + precincts.keySet().stream().map(mp -> mp.getId()).collect(Collectors.toList()).toString();   	
    	s = s + precinctDistrictMapping.entrySet().stream()
    			.map(e -> e.getKey().getMaster().getId() + " :  " + e.getValue().getMaster().getID())
    			.collect(Collectors.joining(", "));
    	return s;
    }
}