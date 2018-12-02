package cse308;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import Users.UserAccount;

@RestController
public class SimulationController {
	
	@RequestMapping(value="/api/start" , method = RequestMethod.POST)
	public @ResponseBody UserAccount addNewWorker(@RequestBody UserAccount jsonString) {

	    //do business logic
	    return jsonString;
	}

}
