package springWebshop.application.thymeleafControllers;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.dto.AccountDTO;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.security.UserDetailsServiceImpl;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.SegmentationService;
import springWebshop.application.service.user.AccountService;

@Controller
@SessionAttributes({"sessionModel"})
@RequestMapping("webshop")
public class AccoutController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	@Qualifier("SegmentationServiceImpl")
	SegmentationService productSegmentationService;
	
	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	
	@ModelAttribute("links")
	private LinkedHashMap<String, String> getLinks() {
		LinkedHashMap<String, String> linkMap = new LinkedHashMap<>();
		linkMap.put("Products", "/webshop/products");
		linkMap.put("Shopping Cart", "/webshop/shoppingcart");
		return linkMap;

	}
	
	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService,productSegmentationService);
	}
	

	@GetMapping("login")
	public String getLogin(Model mode) {
		System.out.println("Kom till GET LOGIN");
		
		return "account/login";
	}
	
	@PostMapping("processLogin")
	public String postLogin(Model model, UserDetails user, HttpServletRequest request) {
		System.out.println("Kom till POST LOGIN");
		System.out.println(user);
		return "account/login";
	}
	
	@GetMapping("register")
	public String getRegister( Model model) {
		AccountDTO accountDTO = new AccountDTO();
		model.addAttribute("account", accountDTO);
		return "account/register";
	}
	@PostMapping("register")
	public String postRegister(@Valid AccountDTO account,BindingResult results,
			@ModelAttribute("sessionModel") SessionModel session,
			Model model) {
		if(!results.hasErrors()) {
			System.out.println(account);
			Customer customerFromDTO = customerAccountDTO(account);
			ServiceResponse<Customer> accountResponse = accountService.createCustomer(customerFromDTO);
			if(accountResponse.isSucessful()) {
				Customer c = accountResponse.getResponseObjects().get(0);
				model.addAttribute("newUser", c);
			}
			else {
				model.addAttribute("errorMessage", accountResponse.getErrorMessages());
			}
			return "account/login";
		}
		else {
			model.addAttribute("errorMessage",results.getAllErrors());
			return "account/register";
		}
		
		
	}
	
	@GetMapping("profile")
	public String getProfile(@ModelAttribute("sessionModel") SessionModel session,Model model) {
		
		
		return "account/profile";
	}

	private Customer customerAccountDTO(@Valid AccountDTO account) {
		Customer c = new Customer(account.getFirstName(), account.getLastName(), account.getPassword(), account.getEmail(),
				account.getPhoneNumber(),account.getMobileNumber(),null);
		
	return c;
	
	}
	
	
	
}
