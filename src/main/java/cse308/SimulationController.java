package cse308;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cse308.Simulation.FunctionWeights;
import cse308.Simulation.SimulationManager;
import cse308.Simulation.SimulationParams;
import cse308.Users.UserAccount;

@RestController
public class SimulationController {

	@RequestMapping(value = "/api/start", method = RequestMethod.POST, consumes = "application/json")
	public void addNewWorker(@RequestBody Map<String, Object> simParams) throws Exception {

		Map<String, Object> b = (Map<String, Object>) simParams.get("functionWeights");

		FunctionWeights functionWeights = new FunctionWeights((float) b.get("wCompactness"),
				(float) b.get("wPopulationEquality"), (float) b.get("wPartisanFairness"));

		SimulationParams simulationParams = new SimulationParams(functionWeights, (String) simParams.get("state"),
				(String) simParams.get("algortihm"));
		
		UserAccount userAccount = new UserAccount();
		
		SimulationManager.createSim(userAccount,  simulationParams);
	}

}
