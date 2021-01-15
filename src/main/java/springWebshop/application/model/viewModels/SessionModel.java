package springWebshop.application.model.viewModels;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.model.domain.order.Order.OrderStatus;
import springWebshop.application.service.product.SegmentationService;
import springWebshop.application.service.product.ProductService;

@Getter
@Setter
public class SessionModel {

	private ShoppingCart cart;
	private int productPage;
	private int orderPage;
	private int accountPage;
	private String searchString;
	private SegmentationModelObject categoryModel;
	private OrderStatus orderStatus;
	
	public SessionModel(ProductService productService, SegmentationService productSegmentationService) {
		cart = new ShoppingCart(productService);
		productPage = 1;
		orderPage = 1;
		accountPage = 1;
		searchString = "";
		categoryModel = new SegmentationModelObject();
		categoryModel.setCategories(productSegmentationService.getAllCategories().getResponseObjects());
	}

	@Override
	public String toString() {
		return "SessionModel [productPage=" + productPage + ", categoryModel=" + categoryModel + "]";
	}


}
