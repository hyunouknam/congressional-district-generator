package Areas;


import java.util.HashSet;
import java.util.Set;

public class DistrictForMap{
    private int id;
    private final MasterDistrict master;
    private Map map;
    private Set<PrecinctForMap> precincts;
    
    public DistrictForMap(){
        master=null; //null district, for unassigned precincts
        precincts=new HashSet<>();        
    }
    
    public DistrictForMap(MasterDistrict md){
        master=md;
        precincts=new HashSet<>();
    }
    
    
    public Set<PrecinctForMap> getPrecincts(){
        return precincts;
    }
    
    public Set<PrecinctForMap> getBorderPrecincts(){
        //get the precincts that are on te border of the district
        return null;
    }
    
    /*
    Description:
        //Returns the districts holding the precincts that neighbor a precinct on the border of this districtformap
    */
    public Set<DistrictForMap> getNeighborDistricts(){        
        Set<DistrictForMap> neighbors=new HashSet<>();
        for(PrecinctForMap border: getBorderPrecincts()){
            for(PrecinctForMap neighbor: border.getNeighborPrecincts()){
                neighbors.add(neighbor.getParentDistrict());
            }
        }
        return neighbors;
    }
    
    public Map getMap(){
        return map;
    }
    public void setMap(Map m){
        map=m;
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int i){
        id=i;
    }
    
    public MasterDistrict getMaster(){
        return master;
    }
}