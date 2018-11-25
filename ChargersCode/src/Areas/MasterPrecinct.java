package Areas;


import Data.GeoRegion;
import Data.DemographicInfo;

public class MasterPrecinct{
    int id;
    String name;
    GeoRegion geoData;
    MasterPrecinct[] neighboringPrecincts;
    DemographicInfo populationData;
    
    public MasterPrecinct[] getNeighborPrecincts(){
        return neighboringPrecincts;
    }
}