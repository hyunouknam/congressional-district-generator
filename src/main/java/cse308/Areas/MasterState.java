package cse308.Areas;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	private int numOfDistricts;

	@Transient
	private Map currentMap;
	
	@Transient
	private Map originalMap;

	@OneToMany()
	@JoinColumn(name = "state_id")
	private Set<MasterDistrict> districts;

	@OneToMany()
	@JoinColumn(name = "state_id")
	private Set<MasterPrecinct> precincts;

	public MasterState(String name, String consText, boolean popIsEst, int numOfDistricts) {
		this.name = name;
		this.consText = consText;
		this.numOfDistricts = numOfDistricts;
		

//        precinctRepository.findByStateId(this.id).forEach(precincts::add);
//        currentMap = new Map(this);
		districts = new HashSet<>();
		precincts = new HashSet<>();
		currentMap = null;
		originalMap = null;	// used for viewing the original map to compare with new map
	}

	@PostLoad
	public void populateCurrentMap() {
		currentMap = new Map(this, numOfDistricts);
		originalMap = currentMap.clone();
		System.out.println("Here");
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

	public int getNumDistricts() {
		return numOfDistricts;
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

	public int getNumOfDistricts() {
		return numOfDistricts;
	}

	public void setNumOfDistricts(int numOfDistricts) {
		this.numOfDistricts = numOfDistricts;
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
		int prs = precincts.size();
		JSONArray a = new JSONArray(districts);
		JSONObject c = new JSONObject();
		c.put("id", id);
		c.put("name", name);
		c.put("constitution text", consText);
		c.put("number of districts", numOfDistricts);
		c.put("districts", a);
		c.put("precincts", prs);
		c.put("Map", currentMap.toString());
		return c.toString();
	}
	
	public String fetchState() {
		JSONArray a = new JSONArray(districts);
		JSONObject c = new JSONObject();
		c.put("id", id);
		c.put("name", name);
		c.put("constitution text", consText);
		c.put("number of districts", numOfDistricts);
		c.put("districts", a);
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