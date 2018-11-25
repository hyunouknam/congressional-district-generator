package Areas;


import Simulation.Move;
import java.util.HashMap;
import java.util.Set;

public class Map{
    int id;
    MasterState master;
    HashMap<MasterPrecinct, PrecinctForMap> precincts;
    HashMap<MasterDistrict, DistrictForMap> districts;
    DistrictForMap nullDistrict;
    HashMap<PrecinctForMap, DistrictForMap> precinctDistrictMapping;
    
    public HashMap<MasterPrecinct, PrecinctForMap> getAllPrecincts(){
        return precincts; //or return just the precinct for maps?
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
    }
    
    public Map cloneApply() throws CloneNotSupportedException{
        Map newMap=(Map)super.clone();
        //do task on clone
        return newMap;
    }
    
    public MasterState getState(){
        return master;
    }
}