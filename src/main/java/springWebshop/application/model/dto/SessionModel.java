package springWebshop.application.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.integration.account.CustomerAddressRespoitory;
import springWebshop.application.integration.account.CustomerRepository;
import springWebshop.application.model.domain.order.Order.OrderStatus;
import springWebshop.application.model.domain.user.Account;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.service.product.SegmentationService;
import springWebshop.application.service.product.ProductService;

@Getter
@Setter
public class SessionModel {

	
	


	@Override
	public String toString() {
		return "SessionModel [productPage=" + productPage + ", categoryModel=" + categoryModel + "]";
	}
	private ShoppingCartDTO cart;
	private int productPage;
	private int orderPage;
	private int accountPage;
	private String searchString;
	
	private SegmentationModelObject categoryModel;
	private OrderStatus orderStatus;
	
	
	public SessionModel(ProductService productService, SegmentationService productSegmentationService) {
		// Start session as guest
		cart = new ShoppingCartDTO(productService);
		productPage = 1;
		orderPage = 1;
		accountPage = 1;
		searchString = "";
		categoryModel = new SegmentationModelObject();
		categoryModel.setCategories(productSegmentationService.getAllCategories().getResponseObjects());
	}



}
