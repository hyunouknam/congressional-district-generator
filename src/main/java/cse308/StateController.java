package cse308;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cse308.Areas.MasterDistrict;
import cse308.Areas.MasterState;

@RestController
public class StateController {

	@RequestMapping(value = "/api/state", method = RequestMethod.GET, produces = "application/json")
	public String getDistrictsForState() {
		MasterDistrict a = new MasterDistrict("District 1 of NJ");
		a.setID(22);
		return a.jsonFormat();
	}
}