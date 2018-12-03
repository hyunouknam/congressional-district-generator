package cse308;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import cse308.Data.UserAccountRepository;
import cse308.Users.UserAccount;

@Controller
public class LoginController {
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@RequestMapping(value="/api/login", method=RequestMethod.POST, produces="application/json")
	public String property(HttpServletRequest request) {
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    
    	return "redirect:/";
	}
	
	@RequestMapping(value="/api/register", method=RequestMethod.POST, produces="application/json")
	public String addUser(HttpServletRequest request) {
	    String username = request.getParameter("reg-username");
	    String password = request.getParameter("reg-password");
	    System.out.println(username);
	    System.out.println(password);
	    
	    UserAccount user = new UserAccount(username, password);
	    
	    userAccountRepository.save(user);
	    
	    System.out.println(user.toString());
	    String s = "{";
    	s = s + "\"username\":";
    	s = s + "\"" + username + "\"";
    	s = s + ", \"password\":";
    	s = s + "\"" + password + "\"";
    	s = s + "}";
    	return s;
	}
}
	
//	public boolean userExists(String username, String password) {
//		if(username.equals("abc@sbu.edu")) {
//			if(password.equals("1234")) {
//				return true;
//			}
//		}
//		return false;
//	}
//}
