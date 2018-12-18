package cse308.Areas;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import org.json.JSONArray;
import org.json.JSONObject;
import cse308.Simulation.Move;

@Entity
@Table(name = "state")
@Immutable
public class MasterState {

	@Id
	private String id;
	private String name;
	private String consText;

	@Transient
    //Current map is the 2010 districting map
	private Map currentMap;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	private Set<MasterDistrict> districts;
	
	

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	private Set<MasterPrecinct> precincts;

	public MasterState() {
		districts = new HashSet<>();
		precincts = new HashSet<>();
		currentMap = null;
	}

	public MasterState(String name, String consText, boolean popIsEst) {
		this.name = name;
		this.consText = consText;
		

//        precinctRepository.findByStateId(this.id).forEach(precincts::add);
//        currentMap = new Map(this);
		districts = new HashSet<>();
		precincts = new HashSet<>();
		currentMap = null;
	}

	@PostLoad
	public void populateCurrentMap() {
		currentMap = new Map(this);
		System.out.println("Loading Map for MAster State");
		for(PrecinctForMap p: currentMap.getAllPrecincts()) {
			MasterDistrict dist = p.getMaster().getDefaultDistrict();
			DistrictForMap distForMap = currentMap.getDistrict(dist);
			if(distForMap!=null) {
				currentMap.apply(new Move(p, distForMap));
			}
        }
	}

	public Set<MasterPrecinct> findByStateId(int stateId) {
//    	return precinctRepository.findByStateId(stateId);
		return null;
	}

	public String getName() {
		return name;
	}

	public String getConsText() {
		return consText;
	}

	public Map getCurrentMap() {
		return currentMap;
	}

	public Set<MasterDistrict> getDistricts() {
		return districts;
	}

	public Set<MasterPrecinct> getPrecincts() {
		return precincts;
	}

	public String getID() {
		return id;
	}

	public void setID(String i) {
		id = i;
	}

	public void setDistricts(Set<MasterDistrict> districts) {
		this.districts = districts;
	}

	public void setPrecincts(Set<MasterPrecinct> precincts) {
		this.precincts = precincts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConsText(String consText) {
		this.consText = consText;
	}

	public void setCurrentMap(Map currentMap) {
		this.currentMap = currentMap;
	}

	public String toJSON() {
		// convert each district to a JSON object
		JSONArray districtArray	= new JSONArray(
				districts.stream()
						.map(d -> d.toJSON())
						.collect(Collectors.toList()));

		int prs = precincts.size();
		JSONObject c = new JSONObject();
		c.put("id", id);
		c.put("name", name);
		c.put("constitution text", consText);
		c.put("districts", districtArray);
		c.put("precincts", prs);
		c.put("Map", currentMap.toString());
		return c.toString();
	}
	
	public String fetchState() {
		/*
		Since we're loading precinct data from the topo file, we don't actually need to send it up
		all we need to send up is the state info, masterdistricts, and defaultmap
		 */
	    // convert each district to a JSON object
		JSONArray districtArray	= new JSONArray(
				districts.stream()
                    .map(d -> d.toJSON())
                    .collect(Collectors.toList()));

		JSONObject c = new JSONObject();
		c.put("id", id);
		c.put("name", name);
		c.put("constitution text", consText);
		c.put("districts", districtArray);
		c.put("default_map", this.currentMap.toJSON());
		return c.toString();
	}
	
	public String toString() {
		String s = "id: " + id;
		for(MasterDistrict d: districts) {
			s = s + d.toString();
		}
		for(MasterPrecinct p: precincts) {
			s = s + p.toString();
		}
		return s;
	}
}