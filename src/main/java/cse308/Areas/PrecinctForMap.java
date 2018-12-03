package cse308.Areas;

import java.util.HashSet;
import java.util.Set;

public class PrecinctForMap{
    private int id;
    private final MasterPrecinct master;
    private DistrictForMap parentDistrict;
    private Map map;
    public boolean isAssigned=false;
    
    public PrecinctForMap(MasterPrecinct mp){
        master=mp;
    }
    
    public PrecinctForMap(MasterPrecinct mp, DistrictForMap parent){
        master=mp;
        parentDistrict=parent;
    }
    
    public DistrictForMap getParentDistrict(){
        return parentDistrict;
    }
    
    public boolean isDistrictBorder(){
        //find if its on border of its districtformap
        return false;
    }
    
    /*
    Description:
        Gets the collection of neighbors from the parent
        Adds the corressponding PrecinctForMaps within its map, using the MasterPrecint to PrecintForMap pairings
    */
    public Set<PrecinctForMap> getNeighborPrecincts(){
        Set<PrecinctForMap> neighbors=new HashSet<>();
        for(MasterPrecinct mp: master.getNeighbors()){
            neighbors.add(map.getPrecinct(mp));
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
    
    public void setParentDistrict(DistrictForMap d){
        parentDistrict=d;
    }
    public MasterPrecinct getMaster(){
        return master;
    }
}