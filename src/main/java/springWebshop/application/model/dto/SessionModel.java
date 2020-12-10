package springWebshop.application.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.integration.account.CustomerAddressRespoitory;
import springWebshop.application.integration.account.CustomerRepository;
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

	private Customer user;
	private ShoppingCartDTO cart;
	private int productPage;
	private CategoryModelObject categoryModel;
	
	public SessionModel(ProductService productService, SegmentationService productSegmentationService, CustomerRepository custRepo) {
		// Start session as guest
		cart = new ShoppingCartDTO(productService);
		List<CustomerAddress> addresses = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			CustomerAddress address = new CustomerAddress("Storgatan " + j, (j*33), "City X", "Sweden");
			addresses.add(address);
		}
		
		user = new Customer("Johannes", "Hedman", "Password", "Email@email.com", "12341234", "123412123",addresses);
		custRepo.save(user);
		productPage = 1;
		categoryModel = new CategoryModelObject();
		categoryModel.setCategories(productSegmentationService.getAllCategories());
	}


}
