package cse308;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cse308.Data.UserAccountRepository;
//import cse308.Data.UserAccountRepository;
import cse308.Users.UserAccount;

@Controller
public class LoginController {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@RequestMapping(value = "/api/login", method = RequestMethod.POST, produces = "application/json")
	public String login(HttpServletRequest request) {
		System.out.println("Here we go");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		System.out.println(username);
		System.out.println(password);

		UserAccount user = userAccountRepository.findByEmail(username);
		if (user != null && user.getPassword().equals(password)) {
			request.getSession().setAttribute("user", user.getEmail());
			return "redirect:/";
		}

		return "redirect:/usernotfound";
	}

	@RequestMapping(value = "/api/logout", method = RequestMethod.GET, produces = "application/json")
	public String property(HttpServletRequest request) {

		if (request.getSession().getAttribute("user") != null) {
			request.getSession().removeAttribute("user");
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/api/register", method = RequestMethod.POST, produces = "application/json")
	public String register(HttpServletRequest request) {
		;
		String username = request.getParameter("reg-username");
		String password = request.getParameter("reg-password");
		System.out.println(username);
		System.out.println(password);

		UserAccount user = new UserAccount(username, password);

		UserAccount savedUser = userAccountRepository.save(user);
		request.getSession().setAttribute("user", savedUser.getEmail());
		return "redirect:/";
	}
}
