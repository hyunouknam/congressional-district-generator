package cse308.Areas;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import cse308.Data.GeoRegion;
import cse308.Data.GeometryConverter;

@Entity
@Table(name="precinct")
@Immutable
public class MasterPrecinct implements GeoRegion{

    
//    @GeneratedValue(strategy=GenerationType.AUTO)
	@Id
    private String id;
    private String name;
    
    @Column(columnDefinition = "JSON")
    @Convert(converter = GeometryConverter.class)
    private Geometry geometry;
    
    private int population;
    private int votingPopulation;
    private double averageDeomcratVotes;
    private int totalVotes;
    
    @ManyToOne
    @JoinColumn(name = "district_id")
    private MasterDistrict defaultDistrict;
    
    @ManyToOne
    @JoinColumn(name = "state_id")
    private MasterState state;
    
//    @Transient
//    @Column(columnDefinition = "LONGTEXT")
//    @Convert(converter = NeighborsConverter.class)
    @OneToMany
    private Set<MasterPrecinct> neighboringPrecincts;
    
    public MasterPrecinct() {
    	neighboringPrecincts = new HashSet<>();
    }
    
    public MasterPrecinct(String id, String name, int population, double averageDeomcratsVotes,
			int totalVotes) {
		super();
		this.id = id;
		this.name = name;
		this.population = population;
		this.averageDeomcratVotes = averageDeomcratsVotes;
		this.totalVotes = totalVotes;
	}
    
	public MasterPrecinct(String id, String name, Geometry geoData, int population, double averageDeomcratsVotes,
			int totalVotes, Set<MasterPrecinct> neighboringPrecincts) {
		super();
		this.id = id;
		this.name = name;
		this.geometry = geoData;
		this.population = population;
		this.averageDeomcratVotes = averageDeomcratsVotes;
		this.totalVotes = totalVotes;
		this.neighboringPrecincts = neighboringPrecincts;
	}
	
	public MasterDistrict getDefaultDistrict() {
		return defaultDistrict;
	}

	public void setDefaultDistrict(MasterDistrict district) {
		this.defaultDistrict = district;
	}


	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public Set<MasterPrecinct> getNeighboringPrecincts() {
		return neighboringPrecincts;
	}

	public void setNeighboringPrecincts(Set<MasterPrecinct> neighboringPrecincts) {
		this.neighboringPrecincts = neighboringPrecincts;
	}
	
	public String toJSON() {
		GeoJSONWriter w = new GeoJSONWriter();
		GeoJSON j = w.write(geometry);
		String s = j.toString();
		
		JSONObject a = new JSONObject(s);
		JSONArray b = a.getJSONArray("coordinates");
		
//		JSONArray d = new JSONArray(neighboringPrecincts);
		
		JSONObject c = new JSONObject();
		JSONObject geo = new JSONObject();
		geo.put("type", geometry.getGeometryType());
		geo.put("coordinates", b);
		c.put("id", id);
		c.put("name", name);
		c.put("geoData", geo);
		c.put("population", population);
		c.put("votingPopulation", votingPopulation);
		c.put("averageDeomcratsVotes", averageDeomcratVotes);
		c.put("totalVotes", totalVotes);
		c.put("district", defaultDistrict.getID());
		c.put("state", state.getName());
		c.put("neighbors", neighboringPrecincts.size());
		
		return c.toString();
	}

	@Override
	public Geometry getGeometry() {
		return geometry;
	}
	@Override
	public int getPopulation() {
		return population;
	}
	@Override
	public int getTotalVotes() {
		return totalVotes;
	}
	@Override
	public int getVotingPopulation() {
		return votingPopulation;
	}

	@Override
	public double getPercentDemocrat() {
		return averageDeomcratVotes;
	}
	
	public String toString() {
		return " (id: " + this.id + " district: " + this.defaultDistrict.getID() + "java: " + System.identityHashCode(this.defaultDistrict) + ")";
	}

	public String fetchMasterPrecinct() {
		GeoJSONWriter w = new GeoJSONWriter();
		GeoJSON j = w.write(geometry);
		String s = j.toString();
		
		JSONObject c = new JSONObject();
		c.put("id", id);
		c.put("name", name);
		c.put("geometry", s);
		c.put("population", population);
		c.put("votingPopulation", votingPopulation);
		c.put("averageDeomcratVotes", averageDeomcratVotes);
		c.put("totalVotes", totalVotes);
		return c.toString();
	}
	
	
    
//    public MasterPrecinct(String name, Geometry geo, MasterPrecinct[] neighbors, DemographicInfo pop){
//        this.name=name;
//        geoData=geo;
//        neighboringPrecincts=neighbors;
//        populationData=pop;
//    }
    
    
    
    
}