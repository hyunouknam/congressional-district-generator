package cse308;

import java.util.Map;
import java.util.Optional;

import cse308.Data.UserAccountRepository;
import cse308.Simulation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cse308.Areas.MasterDistrict;
import cse308.Areas.MasterPrecinct;
import cse308.Areas.MasterState;
import cse308.Data.StateRepository;
import cse308.Users.UserAccount;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@RestController
public class SimulationController {

	@Autowired
	private StateRepository staterepository;

	@Autowired
	private SimulationManager simulationManager;

	@Autowired
	private UserAccountRepository userRepo;

	@RequestMapping(value = "/api/startSimulation", method = RequestMethod.POST, consumes = "application/json")
	public void addNewWorker(HttpServletRequest request, @RequestBody Map<String, Object> simParams) throws Exception {

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


		// GET USER
		UserAccount user = null;
		Object emailObj = request.getSession().getAttribute("user");
		if(emailObj != null) {
			String email = (String) emailObj;
			user = userRepo.findByEmail(email);
		}
		if(user == null){ user = userRepo.findByEmail("TEMP");}
		if(user == null){ user = new UserAccount();}


		Simulation sim = simulationManager.createSim(user,  simulationParams);


		System.out.println(String.format("Made new sim: %d", sim.getId()));


		//SimulationManager.getInstance().getSimWorker().runNextSimulation();
	}

}