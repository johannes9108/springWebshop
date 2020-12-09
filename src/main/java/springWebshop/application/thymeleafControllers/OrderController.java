package springWebshop.application.thymeleafControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import springWebshop.application.model.domain.Order;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderService;

@Controller
@RequestMapping("webshop")
@SessionAttributes({ "sessionModel" })
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping("checkout")
	public String getCheckout(@ModelAttribute SessionModel sessionModel, Model m) {
		if(sessionModel==null)
			return "redirect:/webshop/products";

		return "checkoutView";
	}
	
	@PostMapping("checkout")
	public String postCheckout(@ModelAttribute SessionModel sessionModel, Model m) {
		//TODO Validation, navigation
		
		System.out.println(sessionModel.getCart().getTotalItems());
		System.out.println(sessionModel.getUser().getId());
		System.out.println(sessionModel.getUser().getAddresses().get(0));
		ServiceResponse<Order> response = orderService.createOrderFromShoppingCart(sessionModel.getCart(), sessionModel.getUser().getId(), sessionModel.getUser().getAddresses().get(0));
		System.out.println(response);
		if(response.isSucessful()) {
			m.addAttribute("completedOrder", response.getResponseObjects().get(0));
			sessionModel.getCart().dispose();
			return "orderCompletedView";
		}
		else {
			m.addAttribute("errorMessage", response.getErrorMessages());
			return "redirect:/webshop/checkout";
		}
		
		//Redirect to OrderCompleted or myaccount
	}

}
