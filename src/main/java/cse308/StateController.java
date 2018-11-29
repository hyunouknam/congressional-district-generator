package cse308;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Areas.MasterState;

@RestController
public class StateController {

//    @RequestMapping("/api/state")
//    public MasterState getState(@RequestParam(value="name", defaultValue="World") String name) {
//    	System.out.println("Heree");
//    	MasterState s = new MasterState(1, "New Jersey", "Constituion Text", true, 12);
//    	System.out.println(s.toString());
//        return s;
//    }
    
    @PostMapping("/api/login")
    public String getStateDistricts(@RequestParam(value="name") String name) {
    	String s = "abc";
    	System.out.print(s);
        return s;
    }
    
    @GetMapping("/api/login")
    public String getStateistricts(@RequestParam(value="name") String name) {
    	String s = "abcd";
    	System.out.print(s);
        return s;
    }
}