package cse308.Areas;

import java.util.HashSet;
import java.util.Set;

import org.locationtech.jts.geom.Geometry;

import cse308.Data.GeoRegion;

public class PrecinctForMap implements GeoRegion{
    private final MasterPrecinct master;
    private final Map map;
    public boolean isAssigned=false;
    
    public PrecinctForMap(MasterPrecinct mp, Map map){
        master=mp;
        this.map=map;
    }
    
    public DistrictForMap getParentDistrict(){
        return this.map.getPrecinctDistrictMapping().get(this);
    }
    
    public boolean isDistrictBorder(){
        Set<PrecinctForMap> precincts = getNeighborPrecincts();
        for(PrecinctForMap pr: precincts) {
        	if(pr.getParentDistrict()!=this.getParentDistrict())return true;
        }
        return false;
    }
    
    /*
    Description:
        Gets the collection of neighbors from the parent
        Adds the corressponding PrecinctForMaps within its map, using the MasterPrecint to PrecintForMap pairings
    */
    public Set<PrecinctForMap> getNeighborPrecincts(){
        Set<PrecinctForMap> neighbors=new HashSet<>();
        for(MasterPrecinct mp: master.getNeighboringPrecincts()){
            neighbors.add(map.getPrecinct(mp));
        }
        return neighbors;
    }
    
    public Set<DistrictForMap> getNeighborDistricts(){
    	Set<DistrictForMap> neighborDistricts=new HashSet<>();
        for(PrecinctForMap np: getNeighborPrecincts()){
        	DistrictForMap d = np.getParentDistrict();
            if(d!=this.getParentDistrict()){
                neighborDistricts.add(d); //adds each district bordering the previosuly chosen district
            }
        }
        return neighborDistricts;
    }
    
    public Map getMap(){
        return map;
    }
    
    public MasterPrecinct getMaster(){
        return master;
    }

	@Override
	public int getPopulation() {
		return master.getPopulation();
	}

	@Override
	public int getVotingPopulation() {
		return master.getVotingPopulation();
	}

	@Override
	public int getTotalVotes() {
		return master.getTotalVotes();
	}

	@Override
	public double getPercentDemocrat() {
		return master.getPercentDemocrat();
	}

	@Override
	public Geometry getGeometry() {
		return master.getGeometry();
	}
}