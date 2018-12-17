package cse308;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cse308.Areas.MasterDistrict;
import cse308.Areas.MasterPrecinct;
import cse308.Areas.MasterState;
import cse308.Data.StateRepository;
import cse308.Simulation.FunctionWeights;
import cse308.Simulation.RegionGrowingParams;
import cse308.Simulation.SimulatedAnnealingParams;
import cse308.Simulation.SimulationManager;
import cse308.Simulation.SimulationParams;
import cse308.Users.UserAccount;
import java.util.HashSet;

@RestController
public class SimulationController {

	@Autowired
	private StateRepository staterepository;
	
	@RequestMapping(value = "/api/startSimulation", method = RequestMethod.POST, consumes = "application/json")
	public void addNewWorker(@RequestBody Map<String, Object> simParams) throws Exception {

		Map<String, Object> b = (Map<String, Object>) simParams.get("functionWeights");

		FunctionWeights functionWeights = new FunctionWeights(
				(float)(double) b.get("wCompactness"),
				(float)(double) b.get("wPopulationEquality"),
				(float)(double) b.get("wPartisanFairness"));
		
		Optional<MasterState> obj = staterepository.findById((String) simParams.get("state"));
                MasterState state = obj.get();
               // MasterState state=new MasterState();
                                
		String algorithm=(String) simParams.get("algorithm");                
                Integer districts=(Integer)simParams.get("districts");
                SimulationParams simulationParams;
                if(algorithm.equals("REGION_GROWING")){
                    simulationParams = new RegionGrowingParams(
                        functionWeights,
                        state,
                        algorithm
                    );
                }
                else{
                    simulationParams = new SimulatedAnnealingParams(
                        functionWeights,
                        state,
                        algorithm,
                        new cse308.Areas.Map(state)
                    );
                }
		
		UserAccount userAccount = new UserAccount();
		
		SimulationManager.getInstance().createSim(userAccount,  simulationParams);
		
		SimulationManager.getInstance().getSimWorker().runNextSimulation();
	}

}