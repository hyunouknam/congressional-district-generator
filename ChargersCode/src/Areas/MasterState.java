package Areas;

import java.util.Set;

public class MasterState{
    private int id;
    private String name;
    private String consText;
    private boolean popIsEst;
    private int numOfDistricts;
    private Map currentMap;
    private Set<MasterDistrict> districts;
    private Set<MasterPrecinct> precincts;
    
    public Map getCurrentMap(){
        //pull data from database
        //create map object
        return null;
    }
    
    public Set<MasterDistrict> getDistricts(){
        return districts;
    }
    
    public Set<MasterPrecinct> getPrecincts(){
        return precincts;
    }
}