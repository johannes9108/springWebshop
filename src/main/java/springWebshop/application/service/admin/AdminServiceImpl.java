package springWebshop.application.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
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
		try {
		Product persistedProduct = productService.getProductById(product.getId()).getResponseObjects().get(0);
		persistedProduct = product;
		System.out.println(typeId);
		ServiceResponse<ProductType> typeResponse = segmentationService.getProductTypeById(typeId);
		System.out.println("TYPE:" + typeResponse.getResponseObjects().get(0));
		persistedProduct.setProductType(typeResponse.isSucessful()?typeResponse.getResponseObjects().get(0)
				:
					segmentationService.getProductTypeById(1L).getResponseObjects().get(0));
		return productService.update(persistedProduct);
		}catch(Exception e) {
			System.out.println(e);
			ServiceResponse<Product> response = new ServiceResponse<Product>();
			response.addErrorMessage(e.getLocalizedMessage());
			return response;
		}
		


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
				// TODO NOT WORKING WHEN SELECTING 0/ALL
				// TODO EXTRACT ALL THIS LOGIC TO SERVICE CLASSES AS WELL IN THE PRODUCT
				// CONTROLLER
				long newType = categoryModelObject.getSelectedType();
				updateProduct(product,newType<=1?1:newType);
				response.addResponseObject(product);
				
		

		}
		return response;
	}

	@Override
	public ServiceResponse<Product> createProduct(Product newProduct,SegmentationModelObject segmentationModel) {
			ServiceResponse<Product> response = new ServiceResponse<Product>();
			try {
				ServiceResponse<ProductType> typeResponse = segmentationService.getProductTypeById(segmentationModel.getSelectedType());
				if(typeResponse.isSucessful()) {
					newProduct.setProductType(typeResponse.getResponseObjects().get(0));
					ServiceResponse<Product> productResponse = productService.create(newProduct);
					if(productResponse.isSucessful()) {
						return  productResponse;
					}
					else {
						response.setErrorMessages(productResponse.getErrorMessages());
						return response;
					}
				}else {
					response.addErrorMessage("Couldn't fetch product type");
					return response;
				}
			}catch(Exception e) {
				System.err.println(e);
				response.addErrorMessage(e.getLocalizedMessage());
				return response;
			}
	}

	@Override
	public ServiceResponse<Integer> createNewSegmentation(String string, SegmentationModelObject segmentationModel) {
		return segmentationService.create(string,segmentationModel);
	}

	


}
