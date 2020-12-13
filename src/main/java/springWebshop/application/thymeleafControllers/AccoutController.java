package springWebshop.application.thymeleafControllers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccoutController {

	@GetMapping("login")
	public String getLogin(Model model) {
		
		
		return "account/login";
	}
	
	@PostMapping("login")
	public String postLogin(Model model, UserDetails user) {
		System.out.println("????");
		System.out.println(user);
		return "account/login";
	}
	
	@GetMapping("register")
	public String getRegister(Model model) {
		return "account/register";
	}
	@PostMapping("register")
	public String postRegister(Model model) {
		return "account/register";
	}
	
	
	
}
