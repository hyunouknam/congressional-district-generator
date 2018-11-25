package Areas;


import java.util.Set;

public class DistrictForMap{
    int id;
    MasterDistrict instanceFor;
    Map map;
    
    
    public Set<PrecinctForMap> getPrecincts(){
        return null;
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
}