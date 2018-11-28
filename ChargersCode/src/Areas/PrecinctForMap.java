package Areas;


import java.util.Set;

public class PrecinctForMap{
    private int id;
    private MasterPrecinct master;
    private Map map;
    
    public PrecinctForMap(MasterPrecinct mp){
        master=mp;
    }
    
    public MasterDistrict getParentDistrict(){
        //return district it's in (DistictForMap) or return district master is in (MasterDistrict)?
        return null;
    }
    
    public boolean isDistrictBorder(){
        //find if its on border of its districtformap
        return false;
    }
    public Set<MasterPrecinct> getNeighborPrecincts(){
        //same as master precincts neighbors? or just neighbors within its districtformap?
     return null;
    }
    
    public Map getMap(){
        return map;
    }
    public void setMap(Map m){
        map=m;
    }
}