package springWebshop.application.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.dto.SegmentationModelObject;
import springWebshop.application.model.dto.SegmentDTO;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.SegmentationService;

@Service("AdminServiceImpl")
public class AdminServiceImpl implements AdminService {

	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;
	@Autowired
	@Qualifier("SegmentationServiceImpl")
	SegmentationService segmentationService;

	
	@Override
	public ServiceResponse<Product> updateProduct(Product product, long typeId) {
		Product persistedProduct = productService.getProductById(product.getId()).getResponseObjects().get(0);
		persistedProduct = product;
		ServiceResponse<ProductType> typeResponse = segmentationService.getProductTypeById(typeId);
		persistedProduct.setProductType(typeResponse.isSucessful()?typeResponse.getResponseObjects().get(0)
				:
			segmentationService.getProductTypeById(1L).getResponseObjects().get(0));

		productService.update(persistedProduct);

		return null;
	}

	@Override
	public ServiceResponse<Order> updateOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResponse<Product> updateSegmentationOfProduct(String action, SegmentationModelObject categoryModelObject, long id) {
		ServiceResponse<Product> response = productService.getProductById(id);
		if (response.isSucessful()) {
			Product product = response.getResponseObjects().get(0);
			switch (action) {
			case "type":
				// TODO NOT WORKING WHEN SELECTING 0/ALL
				// TODO EXTRACT ALL THIS LOGIC TO SERVICE CLASSES AS WELL IN THE PRODUCT
				// CONTROLLER
				long newType = categoryModelObject.getSelectedType();
				ServiceResponse<ProductType> typeResponse = segmentationService.getProductTypeById(newType == 0 ? 1 : newType);
				product.updateProductType(typeResponse.getResponseObjects().get(0));
				updateProduct(product,id);
				response.addResponseObject(product);
				break;
			case "sub":
//				long newSub = categoryModelObject.getSelectedType();
//
//				product.updateProductSub(segmentationService.getProductSubCategoryById(newSub == 0 ? 1 : newSub).get());
//				update(product);
				break;
			case "cat":

				break;
			default:
				break;
			}

		}
		return response;
	}

	@Override

	public ServiceResponse<SegmentationModelObject> fullSegmentation(Product currentProduct) {
		ServiceResponse<SegmentDTO> response = new ServiceResponse<>();
		SegmentationModelObject fullSegmentation = new SegmentationModelObject();
		response = segmentationService.getAllCategories();
		fullSegmentation.setCategories(response.getResponseObjects());
		response = segmentationService.getAllSubCategories();
		fullSegmentation.setSubCategories(response.getResponseObjects());
		response = segmentationService.getAllTypes();
		fullSegmentation.setTypes(response.getResponseObjects());

		fullSegmentation
				.setSelectedCat(currentProduct.getProductType().getProductSubCategory().getProductCategory().getId());
		fullSegmentation.setSelectedSub(currentProduct.getProductType().getProductSubCategory().getId());
		fullSegmentation.setSelectedType(currentProduct.getProductType().getId());
		ServiceResponse<SegmentationModelObject> returningResponse = new ServiceResponse<>();
		returningResponse.addResponseObject(fullSegmentation);
		return returningResponse;
	}


}
