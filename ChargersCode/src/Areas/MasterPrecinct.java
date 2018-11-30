package Areas;


import Data.GeoRegion;
import Data.DemographicInfo;

public class MasterPrecinct{
    private int id;
    private final String name;
    private final GeoRegion geoData;
    private final MasterPrecinct[] neighboringPrecincts;
    private final DemographicInfo populationData;
    
    public MasterPrecinct(String name, GeoRegion geo, MasterPrecinct[] neighbors, DemographicInfo pop){
        this.name=name;
        geoData=geo;
        neighboringPrecincts=neighbors;
        populationData=pop;
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int i){
        id=i;
    }
    
    public String getName(){
        return name;
    }
    
    public GeoRegion getGeoData(){
        return geoData;
    }
    public MasterPrecinct[] getNeighbors(){
        return neighboringPrecincts;
    }
    
    public DemographicInfo getPopulationData(){
        return populationData;
    }
}