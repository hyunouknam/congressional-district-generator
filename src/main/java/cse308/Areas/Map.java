package cse308.Areas;

import java.util.Collection;
import java.util.HashMap;

import cse308.Simulation.FunctionWeights;
import cse308.Simulation.Move;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

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
    private double goodness;

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
        for(MasterDistrict md: master.getDistricts()){
            DistrictForMap d = new DistrictForMap(md, this);
            districts.put(md, d);    
            
            if(d.getMaster().getID().equals("CT_NULL")) {
            	nullDistrict = d;
            }else if(d.getMaster().getID().equals("NJ_NULL")){
            	nullDistrict = d;
            }else if(d.getMaster().getID().equals("NE_NULL")) {
            	nullDistrict = d;
            }
        }
        
        // Initialize, all precincts start in null district
        this.precinctDistrictMapping = new HashMap<>();
        for(PrecinctForMap p: getAllPrecincts()) {
            precinctDistrictMapping.put(p, nullDistrict);
        }
    }

	// ======= GETTERS0

    public MasterState getState(){ return master; }
    public Collection<PrecinctForMap> getAllPrecincts(){ return precincts.values(); }
    public Collection<DistrictForMap> getAllDistricts(){ return districts.values(); }
    public Collection<MasterDistrict> getMasterDistricts(){ return districts.keySet(); }
    public HashMap<MasterDistrict, DistrictForMap> getDistricts(){ return districts; }
    public DistrictForMap getNullDistrict(){ return nullDistrict; }

    public HashMap<PrecinctForMap, DistrictForMap> getPrecinctDistrictMapping(){ return precinctDistrictMapping; }

    public PrecinctForMap getPrecinct(MasterPrecinct precinct){ return precincts.get(precinct); }
    public DistrictForMap getDistrict(MasterDistrict district){ return districts.get(district); }


    // ========== Rest of it

    public void apply(Move m){
    	PrecinctForMap p = getPrecinct(m.getPrecinct());
    	DistrictForMap newD = getDistrict(m.getNewDistrict());
//    	if(p.getMap() != this) {throw new RuntimeException("PRECINCT MISMATCH"); }
//    	if(d.getMap() != this) {throw new RuntimeException("DISTRICT MISMATCH"); }
    	
//    	p.hashCode();
//    	p.getMap().hashCode();
//    	p.getParentDistrict().hashCode();
//    	m.getNewDistrict().hashCode();
//    	
//    	System.out.println(String.format("doing move for map;%x: P-%s;%x, D-%s;%x -> D-%s;%x", 
//
//    	    	p.getMap().hashCode(), 
//    			p.getMaster().getId(), p.hashCode(),
//    				p.getParentDistrict().getMaster().getID(), p.getParentDistrict().hashCode(),
//    				m.getNewDistrict().getMaster().getID(), m.getNewDistrict().hashCode()) );
        
    	precinctDistrictMapping.put(p, newD); //modifies precinct to district mapping
        
//    	System.out.println(String.format("did move: P-%s, D-%s;%x", m.getPrecinct().getMaster().getId(), 
//				p.getParentDistrict().getMaster().getID(), p.getParentDistrict().hashCode()) );
//        
//
//        DistrictForMap d_aaa = p.getMap().getPrecinctDistrictMapping().get(p);
//        System.out.println(String.format("new district direct.get id %s;%x,  through p4m.get id%s;%x ", 
//        		precinctDistrictMapping.get(m.getPrecinct()).getMaster().getID(),
//        		precinctDistrictMapping.get(m.getPrecinct()).hashCode(),
//        		d_aaa.getMaster().getID(), d_aaa.hashCode()));
        

  
    }
    
    public Map cloneApply(Move m) {
        Map newMap = clone();
        newMap.apply(m);
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
            MasterDistrict md = getPrecinctDistrictMapping().get(p).getMaster();
            DistrictForMap d_copy = copy.getDistrict(md);

            //assign copy's version of p to copy's version of d
            copy.precinctDistrictMapping.put(p_copy, d_copy);
        }
        return copy;
    }
	
	 public JSONObject toJSON() {
        // Outputs { dist_id: [p_id, p_id,...], ...}
    	JSONObject c = new JSONObject();
    	//System.out.println("Here in Map.toJSON()");
    	for(DistrictForMap district: getAllDistricts()) {
    		Set<PrecinctForMap> precinctsForDistrict = district.getPrecincts();
    		Set<String> idsOfPrecincts = precinctsForDistrict.stream().map(p -> p.getMaster().getId()).collect(Collectors.toSet());
    		JSONArray arrayOfPrecincts = new JSONArray(idsOfPrecincts);
    		c.put(district.getMaster().getID(), arrayOfPrecincts);
    		//System.out.println(district.getMaster().getID());
    	}
    	return c;
    }
    
    public String toString() {
    	String s = "State Name: " + master.getName() + "\n";
    	s = s + districts.keySet().stream().map(md -> md.getID()).collect(Collectors.toList()).toString();
    	s =s + precincts.keySet().stream().map(mp -> mp.getId()).collect(Collectors.toList()).toString();   	
    	s = s + precinctDistrictMapping.entrySet().stream()
    			.map(e -> e.getKey().getMaster().getId() + " :  " + e.getValue().getMaster().getID())
    			.collect(Collectors.joining(",\n"));
    	return s;
    }
    
    public List<DistrictForMap> addDistricts(int numDistricts){
        List<DistrictForMap> newDistricts=new ArrayList<>();
        for (int j=0;j<numDistricts;j++){
            MasterDistrict temp=new MasterDistrict();
            DistrictForMap district=new DistrictForMap(temp, this);
            districts.put(temp, district);
            newDistricts.add(district);
        }
        return newDistricts;
    }
}