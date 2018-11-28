package Areas;


import Simulation.Move;
import java.util.HashMap;
import java.util.Set;

public class Map{
    private int id;
    private MasterState master;
    private HashMap<MasterPrecinct, PrecinctForMap> precincts;
    private HashMap<MasterDistrict, DistrictForMap> districts;
    private DistrictForMap nullDistrict;
    private HashMap<PrecinctForMap, DistrictForMap> precinctDistrictMapping;
    private float currentGoodness;
    
    public Map(MasterState state){
        //should id be static or changed by DB?
        master=state;
        nullDistrict=new DistrictForMap(); //holds unused precincts
        for(MasterPrecinct mp: master.getPrecincts()){
            PrecinctForMap pm=new PrecinctForMap(mp);
            precincts.put(mp, pm);
            nullDistrict.getPrecincts().add(pm);
        }
        for(MasterDistrict md: master.getDistricts()){
            DistrictForMap dm=new DistrictForMap(md);
            districts.put(md, dm);
        }
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
    
    public void assignPrecinct(DistrictForMap d, PrecinctForMap p){
        //add precinctformap to districtformap's collection
    }
    
    public Set<PrecinctForMap> getAllBorderPrecincts(){
        Set<PrecinctForMap> borders=null;
        //add all value in borders for each district OR add values in borders for state
        return borders;
    }
    
    public void apply(Move m){
        //add move to collection of moves?
        //change goodness
        //adjust precinct to district mapping
    }
    
    public Map cloneApply(Move m) throws CloneNotSupportedException{
        Map newMap=(Map)super.clone();
        //do task on clone
        //change goodness
        //adjust precinct to district mapping
        return newMap;
    }
    
    public MasterState getState(){
        return master;
    }
    
    public DistrictForMap getNullDisrict(){
        return nullDistrict;
    }
    
    public boolean isAcceptable(){
        //satisfies all reqs
            //population=total pop of assigned precincts/# of districts
            //contiguous
            //compact
        return false;
    }
    
    public float getGoodness(){
        return currentGoodness;
    }
}