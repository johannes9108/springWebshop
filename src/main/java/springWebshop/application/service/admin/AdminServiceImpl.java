package springWebshop.application.service.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.viewModels.SegmentationModelObject;
import springWebshop.application.security.CustomSuccessHandler;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.SegmentationService;

@Service("AdminServiceImpl")
public class AdminServiceImpl implements AdminService {
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);


	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;
	@Autowired
	@Qualifier("SegmentationServiceImpl")
	SegmentationService segmentationService;

	
	@Override
	public ServiceResponse<Product> updateProduct(Product product, long typeId) {
		try {
		Product persistedProduct = product;
		ServiceResponse<ProductType> typeResponse = segmentationService.getProductTypeById(typeId);
		persistedProduct.setProductType(typeResponse.isSucessful()
				? typeResponse.getResponseObjects().get(0)
				: segmentationService.getProductTypeById(1L).getResponseObjects().get(0));
		return productService.update(persistedProduct);
		}catch(Exception e) {
			logger.error(e.toString());
			ServiceResponse<Product> response = new ServiceResponse<Product>();
			response.addErrorMessage(e.getLocalizedMessage());
			return response;
		}
	}

	@Override
	public ServiceResponse<Product> createProduct(Product newProduct,SegmentationModelObject segmentationModel) {
			ServiceResponse<Product> response = new ServiceResponse<Product>();
			try {
				ServiceResponse<ProductType> typeResponse = segmentationService.getProductTypeById(segmentationModel.getSelectedType());
				if(typeResponse.isSucessful()) {
					newProduct.setProductType(typeResponse.getResponseObjects().get(0));
					newProduct.setPublished(true);
					generateRandImg(newProduct);
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
				logger.error(e.toString());
				response.addErrorMessage(e.getLocalizedMessage());
				return response;
			}
	}

	@Override
	public ServiceResponse<Integer> createNewSegmentation(String string, SegmentationModelObject segmentationModel) {
		return segmentationService.create(string,segmentationModel);
	}

	private void generateRandImg(Product newProduct) {
		int rand = new Random(System.nanoTime()).nextInt(3) + 1;
		String baseImgUrl = "/img/";
		Map<Integer, String> imgMap = new HashMap<>();
		imgMap.put(1, baseImgUrl + "dog.jpg");
		imgMap.put(2, baseImgUrl + "phone.jpg");
		imgMap.put(3, baseImgUrl + "laptop.jpg");
		imgMap.put(4, baseImgUrl + "dogThumbnail.jpg");
		imgMap.put(5, baseImgUrl + "phoneThumbnail.jpg");
		imgMap.put(6, baseImgUrl + "laptopThumbnail.jpg");

		newProduct.setFullImageUrl(imgMap.get(rand));
		newProduct.setThumbnailUrl(imgMap.get(rand + 3));
	}



}
