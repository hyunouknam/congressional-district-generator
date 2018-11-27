package Areas;


import java.util.HashSet;
import java.util.Set;

public class DistrictForMap{
    private int id;
    private MasterDistrict instanceFor;
    private Map map;
    private Set<PrecinctForMap> precincts;
    
    public DistrictForMap(){
        instanceFor=null; //null district, for unassigned precincts
        precincts=new HashSet<>();        
    }
    
    public DistrictForMap(MasterDistrict md){
        instanceFor=md;
        precincts=new HashSet<>();
    }
    
    
    public Set<PrecinctForMap> getPrecincts(){
        return precincts;
    }
    
    public Set<PrecinctForMap> getBorderPrecincts(){
        return null;
    }
    
    public Set<DistrictForMap> getNeighborDistricts(){
        //will include the districts belonging to the precincts that neighbor a precinct on the border of this districtformap
        return null;
    }
    
    public Map getMap(){
        return map;
    }
    public void setMap(Map m){
        map=m;
    }
}