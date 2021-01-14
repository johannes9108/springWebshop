package springWebshop.application.service.admin;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.viewModels.SegmentationModelObject;
import springWebshop.application.service.ServiceResponse;

public interface AdminService {
	
	ServiceResponse<Product> updateProduct(Product product, long typeId);
	ServiceResponse<Product> createProduct(Product newProduct, SegmentationModelObject segmentationModelObject);
	ServiceResponse<Integer> createNewSegmentation(String string, SegmentationModelObject categoryModel);
	
	
	

}
