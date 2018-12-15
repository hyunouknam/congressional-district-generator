package cse308.Areas;

import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.tomcat.util.json.JSONParser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cse308.Data.GeometryConverter;

@Entity
@Table(name="precinct")
@Immutable
public class MasterPrecinct{

    
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
    private MasterDistrict district;
    
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
	
	public Geometry getGeometry() {
		return geometry;
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
	public int getPopulation() {
		return population;
	}
	public double getAverageDeomcratsVotes() {
		return averageDeomcratVotes;
	}
	public int getTotalVotes() {
		return totalVotes;
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
		c.put("district", district.getID());
		c.put("state", state.getName());
		c.put("neighbors", neighboringPrecincts.size());
		
		return c.toString();
	}
	
	
    
//    public MasterPrecinct(String name, Geometry geo, MasterPrecinct[] neighbors, DemographicInfo pop){
//        this.name=name;
//        geoData=geo;
//        neighboringPrecincts=neighbors;
//        populationData=pop;
//    }
    
    
    
    
}