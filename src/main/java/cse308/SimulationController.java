package cse308;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Simulation.FunctionWeights;
import Simulation.SimulationManager;
import Simulation.SimulationParams;
import Users.UserAccount;

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
