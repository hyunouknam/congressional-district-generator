package cse308;

import javax.servlet.http.HttpServletRequest;

import cse308.Data.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cse308.Users.UserAccount;

@RestController
public class UserController {
    @Autowired
	private UserAccountRepository userAccountRepository;

	@RequestMapping(value = "/api/getUserData", method = RequestMethod.GET, produces = "application/json")
	public String getUserData(HttpServletRequest request) {
		Object emailObj = request.getSession().getAttribute("user");

		if(emailObj != null) {
			String email = (String) emailObj;
			UserAccount user = userAccountRepository.findByEmail(email);
			if(user != null) {
				return user.toJSON().toString();
			}
		}

		UserAccount userAcc = new UserAccount("TEMP", "");
		return userAcc.toJSON().toString();
	}
	
//	public String getUserSims(UserAccount user) {
//		if (user.getEmail().equals("abc@sbu.edu")) {
//			FunctionWeights w = new FunctionWeights(0.3, 0.5, 0.7);
//			SimulationParams params = new SimulationParams(w, "NJ", "RegionGrowing");
//			Simulation s2 = new RegionGrowingSimulation(user, params);
//			FunctionWeights w1 = new FunctionWeights(0.4, 0.5, 0.6);
//			SimulationParams params1 = new SimulationParams(w1, "CT", "RegionGrowing");
//			Simulation s1 = new RegionGrowingSimulation(user, params1);
//			
//			AbstractCollection<Simulation> sims = new ArrayList<>();
//			sims.add(s1);
//			sims.add(s2);
//			
//			String s = "{";
//			s = s + "\"user\":";
//			s = s + "\"" + user.getEmail() + "\"";
//			s = s + ", \"simulations\": [";
//			
//			for (Simulation sim : sims) {
//				s = s + sim.getJSON() +", ";
//			}
//			s = s.substring(0, s.length()-2);
//			s = s + "]}";
//			return s;
//		}
//		return null;
//	}
}