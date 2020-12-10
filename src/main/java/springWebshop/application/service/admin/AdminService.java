package springWebshop.application.service.admin;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.user.Account;
import springWebshop.application.model.dto.SegmentationModelObject;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductSearchConfig;

public interface AdminService {
	
	ServiceResponse<Product> updateProduct(Product product, long typeId);
	ServiceResponse<Order> updateOrder();
//	ServiceResponse<Account> updateAccount();
	ServiceResponse<Product> updateSegmentationOfProduct(String action, SegmentationModelObject categoryModelObject, long id);
	ServiceResponse<SegmentationModelObject> fullSegmentation(Product currentProduct);
	
	

}
