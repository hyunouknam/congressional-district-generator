package cse308.Data;

import org.locationtech.jts.geom.Geometry;

public interface GeoRegion {
	
	public int getPopulation();
	public int getVotingPopulation();
	public int getTotalVotes();
	public double getPercentDemocrat();
	public Geometry getGeometry();
}
