package springWebshop.application.thymeleafControllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.security.UserDetailsImpl;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.user.AccountService;

@Controller
@RequestMapping("webshop")
@SessionAttributes({ "sessionModel" })
public class OrderController {

	@Autowired
	OrderService orderService;
	@Autowired
	AccountService customerService;
	@ModelAttribute("links")
	private LinkedHashMap<String, String> getLinks() {
		LinkedHashMap<String, String> linkMap = new LinkedHashMap<>();
		linkMap.put("Products","/webshop/products");
		linkMap.put("Shopping Cart","/webshop/shoppingcart");
		return linkMap;
		
	}
	

	@GetMapping("checkout")
	public String getCheckout(@ModelAttribute SessionModel sessionModel, Model m, Authentication authentication) {
		m.addAttribute("linkMap", getLinks());
		if(authentication!=null) {
				long id = ((UserDetailsImpl)authentication.getPrincipal()).getId();
				ServiceResponse<Customer> customerResponse = customerService.getCustomerById(id);
				System.out.println(customerResponse.getResponseObjects());
				System.out.println(customerResponse);
				if(customerResponse.isSucessful()) {
					
					m.addAttribute("customer", customerResponse.getResponseObjects().get(0));
				}
				else {
					m.addAttribute("errorMessage", customerResponse.getErrorMessages());
					m.addAttribute("customer", new Customer());
				}
				
				return "checkoutView";
		}
			return "redirect:/webshop/products";
	}
	
	@PostMapping("checkout")
	public String postCheckout(@ModelAttribute SessionModel sessionModel, Model m,Authentication authentication) {
		//TODO Validation, navigation
		m.addAttribute("linkMap", getLinks());
		
		System.out.println(sessionModel.getCart().getTotalItems());
		//TODO Use ID from Authentication and lookup User
//		System.out.println(sessionModel.getUser().getId());
		
		if(authentication!=null) {
			long id = ((UserDetailsImpl)authentication.getPrincipal()).getId();
			List<CustomerAddress> addresses = new ArrayList<>();
			addresses.add(new CustomerAddress());
		
			ServiceResponse<Order> response = orderService.createOrderFromShoppingCart(sessionModel.getCart(), id,new CustomerAddress());
			System.out.println(response);
			if(response.isSucessful()) {
				m.addAttribute("completedOrder", response.getResponseObjects().get(0));
				sessionModel.getCart().dispose();
				return "orderCompletedView";
			}
			else {
				m.addAttribute("errorMessage", response.getErrorMessages());
			}
			
		}
		return "redirect:/webshop/checkout";
		
		//Redirect to OrderCompleted or myaccount
	}
	@GetMapping("testOrderView")
	public String getOrderCompleteView(Model m) {
		

		m.addAttribute("completedOrder",orderService.getOrderById(1L).getResponseObjects().get(0));
		return "orderCompletedView";
	}

}
