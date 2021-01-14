package springWebshop.application.thymeleafControllers;

import java.util.LinkedHashMap;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.viewModels.SessionModel;
import springWebshop.application.security.UserDetailsImpl;
import springWebshop.application.service.ServiceErrorMessages;
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
		linkMap.put("Products", "/webshop/products");
		linkMap.put("Shopping Cart", "/webshop/shoppingcart");
		return linkMap;

	}

	@GetMapping("checkout")
	public String getCheckout(@ModelAttribute SessionModel sessionModel, Model m, Authentication authentication) {
		m.addAttribute("linkMap", getLinks());
		if (authentication != null) {
			return prepareCheckoutPage(m, authentication);
		}
		return "redirect:/webshop/products";
	}

	@PostMapping("checkout")
	public String postCheckout(@ModelAttribute SessionModel sessionModel, Model m, Authentication authentication,
			@RequestParam("action") Optional<String> action, @RequestParam("actionValue") Optional<Integer> actionValue,
			@Valid Optional<CustomerAddress> newAddress, BindingResult result) {
		if (authentication != null) {

			if (action.isPresent()) {
				long id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

				switch (action.get()) {

				case "newAddress":
					ServiceResponse<Customer> customerResponse;
					return handleNewAddress(m, newAddress, result, id);

				case "newOrder":
					customerResponse = customerService.getCustomerById(id);
					if (customerResponse.isSucessful() && actionValue.isPresent()) {
						return handleNewOrder(sessionModel, m, actionValue, id, customerResponse);
					}
					break;
				}

			}

		}
		return "redirect:/webshop/checkout";
	}

	private String handleNewOrder(SessionModel sessionModel, Model m, Optional<Integer> actionValue, long id,
			ServiceResponse<Customer> customerResponse) {
		Customer foundCustomer = customerResponse.getResponseObjects().get(0);
		ServiceResponse<Order> response = orderService.createOrderFromShoppingCart(sessionModel.getCart(), id,
				foundCustomer.getAddresses().get(actionValue.get()));

		if (response.isSucessful()) {
			m.addAttribute("completedOrder", response.getResponseObjects().get(0));
			sessionModel.getCart().dispose();
			return "orderCompletedView";
		} else {
			m.addAttribute("errorMessage", response.getErrorMessages());
			return "redirect:/webshop/checkout";
		}
	}

	private String handleNewAddress(Model m, Optional<CustomerAddress> newAddress, BindingResult result, long id) {
		if (result.hasErrors()) {
			return "redirect:/webshop/checkout";

		}
		ServiceResponse<Customer> customerResponse = customerService.getCustomerById(id);
		if (customerResponse.isSucessful()) {
			Customer c = customerResponse.getResponseObjects().get(0);
			c.addAddress(newAddress.get());
			ServiceResponse<Customer> res = customerService.updateCustomer(c);
			return "redirect:/webshop/checkout";
		} else {
			m.addAttribute("errorMessage", ServiceErrorMessages.CUSTOMER.couldNotFind());
			return "redirect:/webshop/login";
		}
	}
	private String prepareCheckoutPage(Model m, Authentication authentication) {
		long id = ((UserDetailsImpl) authentication.getPrincipal()).getId();
		ServiceResponse<Customer> customerResponse = customerService.getCustomerById(id);
		if (customerResponse.isSucessful()) {
			System.out.println("THE CUSTOMER TO CHECKOUT:" + customerResponse.getResponseObjects().get(0));
			m.addAttribute("customer", customerResponse.getResponseObjects().get(0));
		} else {
			m.addAttribute("errorMessage", customerResponse.getErrorMessages());
			m.addAttribute("customer", new Customer());
		}
		m.addAttribute("newAddress", new CustomerAddress());

		return "checkoutView";
	}

}
