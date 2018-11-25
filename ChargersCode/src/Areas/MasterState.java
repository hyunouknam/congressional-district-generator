package Areas;

import java.util.Set;

public class MasterState{
    int id;
    String name;
    String consText;
    boolean popIsEst;
    int numOfDistricts;
    Map currentMap;
    Set<MasterDistrict> districts;
    Set<MasterPrecinct> precincts;
    
    public Map getCurrentMap(){
        //pull data from database
        //create map object
        return null;
    }
    
    public Map makeBlankMap(){
        Map blank=new Map();
        return blank;
    }
}