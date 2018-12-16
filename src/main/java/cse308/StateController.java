package cse308;

import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import cse308.Areas.MasterPrecinct;
import cse308.Areas.MasterState;
import cse308.Data.PrecinctRepository;
import cse308.Data.StateRepository;

@RestController
public class StateController {
	
	@Autowired
	private PrecinctRepository prec;
	
	@Autowired
	private StateRepository staterepository;

	@RequestMapping(value = "/api/state", method = RequestMethod.GET, produces = "application/json")
	public String getDistrictsForState() {
		Optional<MasterState> state = staterepository.findById("NJ");
		return state.get().toJSON();
	}
	
	@RequestMapping(value = "/api/simulation", method = RequestMethod.GET, produces = "application/json")
	public String getMap() {
		Optional<MasterState> state = staterepository.findById("CT");
		return state.get().getCurrentMap().serializeMap();
	}
	
	@RequestMapping(value = "/api/fetchInitialStates", method = RequestMethod.GET, produces = "application/json")
	public String fetchInitialStates() {
		StringJoiner joiner = new StringJoiner(",");
		for(MasterState s: staterepository.findAll()) {
		    joiner.add(s.fetchState());
		}
		return "[" + joiner.toString() + "]";
	}
	
	@RequestMapping(value = "/api/fetchPrecinctsByDistrict/{id}", method = RequestMethod.GET, produces = "application/json")
	public String fetchPrecinctsByDistrict(@PathVariable String id) {
		String string = "[";
		Iterable<MasterPrecinct> precincts = prec.findByDefaultDistrictId(id);
		for(MasterPrecinct p: precincts) {
			string = string + p.fetchMasterPrecinct() + ",";
		}
		string = string.substring(0, string.length()-1);
		string += "]";
		return string;
	}
	
	@RequestMapping(value = "/api/prec", method = RequestMethod.GET, produces = "application/json")
	public String getPrecincts() throws JsonProcessingException {
		Optional<MasterPrecinct> pc = prec.findById("3400142202");
		return pc.get().toJSON();
	}
	
	@RequestMapping(value = "/api/state/{id}", method = RequestMethod.GET, produces = "application/json")
	public String getDistrictsForStateById(@PathVariable String id) {
		Optional<MasterState> state = staterepository.findById(id);
	
		return state.get().toString();
	}
	
	@RequestMapping(value = "/api/prec/{id}", method = RequestMethod.GET, produces = "application/json")
	public String getPrecinctsWithId(@PathVariable String id) throws JsonProcessingException {
		Optional<MasterPrecinct> pc = prec.findById(id);
		return pc.get().toJSON();
	}
	
	@RequestMapping(value = "/api/district/{id}", method = RequestMethod.GET, produces = "application/json")
	public int getPrecinctsByDistrict(@PathVariable String id) throws JsonProcessingException {
		Set<MasterPrecinct> pc = prec.findByDefaultDistrictId(id);
		return pc.size();
	}
}