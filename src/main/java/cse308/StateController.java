package cse308;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Areas.MasterDistrict;
import Areas.MasterState;

@RestController
public class StateController {

    @GetMapping("/api/state")
    public MasterDistrict getDistrictsForState() {
    	MasterDistrict a = new MasterDistrict("District 1 of NJ");
    	a.setID(22);
    	return a;
    }
}