package cse308;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import Users.UserAccount;

@Controller
public class LoginController {
	@PostMapping("/api/login")
	public RedirectView property(@ModelAttribute("loginForm") UserAccount user ) {
	    String username = user.getUsername();
	    String password = user.getPassword();
	    
	    if(userExists(username, password)) {
	    	return new RedirectView("/");
	    }
	    return new RedirectView("userNotFound");
	}
	
	public boolean userExists(String username, String password) {
		if(username.equals("abc@sbu.edu")) {
			if(password.equals("1234")) {
				return true;
			}
		}
		return false;
	}
}
