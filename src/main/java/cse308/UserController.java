package cse308;

import java.util.AbstractCollection;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cse308.Areas.MasterDistrict;
import cse308.Areas.MasterState;
import cse308.Simulation.FunctionWeights;
import cse308.Simulation.RegionGrowingSimulation;
import cse308.Simulation.Simulation;
import cse308.Simulation.SimulationParams;
import cse308.Users.UserAccount;

@RestController
public class UserController {
	
	@RequestMapping(value = "/api/getUserData", method = RequestMethod.GET, produces = "application/json")
	public String getUserData(HttpServletRequest request) {
//		System.out.println("HERE33");
//		String user = (String) request.getSession().getAttribute("user");
		String user = "abc@sbu.edu";
		UserAccount userAcc = new UserAccount(user, "");
		
//		JsonBuilderFactory a = Json.createBuilderFactory(null);
//		JsonObjectBuilder b = (JsonObjectBuilder) a.createObjectBuilder()
//				.add("user", "abc@sbu.edu")
//				.add("savedData", a.createArrayBuilder()
//						.add(a.createObjectBuilder()
//								.add("id", "1")
//								.add("state", "nj")
//								.add("weights", a.createObjectBuilder()
//										.add("compactness", 0.3)
//										.add("population_equality", 0.8)
//										.add("partisan_fairness", 0.2)))
//						.add(a.createObjectBuilder()
//								.add("id", "2")
//								.add("state", "ct")
//								.add("weights", a.createObjectBuilder()
//										.add("compactness", 0.4)
//										.add("population_equality", 0.5)
//										.add("partisan_fairness", 0.6)))).build();
//		return b.toString();
		return getUserSims(userAcc);
	}
	
	public String getUserSims(UserAccount user) {
		if (user.getUsername().equals("abc@sbu.edu")) {
			FunctionWeights w = new FunctionWeights(0.3, 0.5, 0.7);
			SimulationParams params = new SimulationParams(w, "NJ", "RegionGrowing");
			Simulation s2 = new RegionGrowingSimulation(user, params);
			FunctionWeights w1 = new FunctionWeights(0.4, 0.5, 0.6);
			SimulationParams params1 = new SimulationParams(w1, "CT", "RegionGrowing");
			Simulation s1 = new RegionGrowingSimulation(user, params1);
			
			AbstractCollection<Simulation> sims = new ArrayList<>();
			sims.add(s1);
			sims.add(s2);
			
			String s = "{";
			s = s + "\"user\":";
			s = s + "\"" + user.getUsername() + "\"";
			s = s + ", \"simulations\": [";
			
			for (Simulation sim : sims) {
				s = s + sim.getJSON() +", ";
			}
			s = s.substring(0, s.length()-2);
			s = s + "]}";
			return s;
		}
		return null;
	}
}
