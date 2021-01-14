package springWebshop.application.thymeleafControllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.viewModels.AccountDTO;
import springWebshop.application.model.viewModels.SessionModel;
import springWebshop.application.security.UserDetailsImpl;
import springWebshop.application.security.UserDetailsServiceImpl;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.SegmentationService;
import springWebshop.application.service.user.AccountService;

@Controller
@SessionAttributes({ "sessionModel" })
@RequestMapping("webshop")
public class AccoutController {

	@Autowired
	AccountService accountService;

	@Autowired
	OrderService orderService;

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
		return new SessionModel(productService, productSegmentationService);
	}

	@GetMapping("login")
	public String getLogin(Model mode, HttpServletRequest request) {
		return "account/login";
	}

	@PostMapping("processLogin")
	public String postLogin(Model model, HttpServletRequest request) {
		return "account/login";
	}

	@GetMapping("register")
	public String getRegister(Model model) {
		AccountDTO accountDTO = new AccountDTO();
		model.addAttribute("account", accountDTO);
		return "account/register";
	}

	@PostMapping("register")
	public String postRegister(@Valid AccountDTO account, BindingResult results,
			@ModelAttribute("sessionModel") SessionModel session, Model model) {
		if (!results.hasErrors()) {
			return registerNewAccount(account, results, model);
		}
		model.addAttribute("errorMessage", results.getAllErrors());
		return "account/register";

	}

	private String registerNewAccount(AccountDTO account, BindingResult results, Model model) {

		Customer customerFromDTO = customerAccountDTO(account);
		ServiceResponse<Customer> accountResponse = accountService.createCustomer(customerFromDTO);
		if (accountResponse.isSucessful()) {
			Customer c = accountResponse.getResponseObjects().get(0);
			model.addAttribute("newUser", c);
		} else {
			model.addAttribute("errorMessage", accountResponse.getErrorMessages());
		}
		return "account/login";

	}

	@GetMapping("profile")
	public String getProfile(@ModelAttribute("sessionModel") SessionModel session, Model model,
			Authentication authentication) {
		if (authentication != null) {
			long id = ((UserDetailsImpl) authentication.getPrincipal()).getId();
			ServiceResponse<Customer> customerResponse = accountService.getCustomerById(id);
			if (customerResponse.isSucessful()) {
				prepareProfileData(model, customerResponse);
				return "account/profile";
			}

		}
		model.addAttribute("errorMessage", "Could not find authenticated info");
		return "account/login";
	}

	private void prepareProfileData(Model model, ServiceResponse<Customer> customerResponse
			) {
		Customer loggedOnCustomer = customerResponse.getResponseObjects().get(0);
		ServiceResponse<Order> orderResponse = orderService.getAllOrdersByCustomerId(loggedOnCustomer.getId());

		List<Order> associatedOrders = null;
		if (orderResponse.isSucessful()) {
			associatedOrders = orderResponse.getResponseObjects();
		}
		model.addAttribute("customer", loggedOnCustomer);
		model.addAttribute("orders", associatedOrders);
	}

	private Customer customerAccountDTO(@Valid AccountDTO account) {
		Customer c = new Customer(account.getFirstName(), account.getLastName(), account.getPassword(),
				account.getEmail(), account.getPhoneNumber(), account.getMobileNumber(),
				new ArrayList<CustomerAddress>());

		return c;

	}

}
