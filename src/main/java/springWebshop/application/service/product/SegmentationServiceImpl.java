package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springWebshop.application.integration.SegmentationDTORepositoryImpl;
import springWebshop.application.integration.product.ProductCategoryRepository;
import springWebshop.application.integration.product.ProductSubCategoryRepository;
import springWebshop.application.integration.product.ProductTypeRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.viewModels.SegmentDTO;
import springWebshop.application.model.viewModels.SegmentationModelObject;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderSerivceImpl;

@Service("SegmentationServiceImpl")
public class SegmentationServiceImpl implements SegmentationService {
	private static final Logger logger = LoggerFactory.getLogger(SegmentationServiceImpl.class);


	@Autowired
	SegmentationDTORepositoryImpl segmentationDTORepository;
	@Autowired
	ProductCategoryRepository productCategoryRepository;
	@Autowired
	ProductSubCategoryRepository productSubCategoryRepository;
	@Autowired
	ProductTypeRepository productTypeRepository;

	@Override
	public ServiceResponse<SegmentDTO> getAllCategories() {

		ServiceResponse<SegmentDTO> response = new ServiceResponse<>();
		try {
			List<SegmentDTO> listOfSegments = segmentationDTORepository.getAllCategoryDTO();
			response.setResponseObjects(listOfSegments);
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_CATEGORY.couldNotFind());
		}
		return response;
	}

	@Override
	public ServiceResponse<SegmentDTO> getAllSubCategories() {
		
		ServiceResponse<SegmentDTO> response = new ServiceResponse<>();
		try {
			List<SegmentDTO> listOfSegments = segmentationDTORepository.getAllSubCategoryDTO();
			response.setResponseObjects(listOfSegments);
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_CATEGORY.couldNotFind());
		}
		return response;
	}

	@Override
	public ServiceResponse<SegmentDTO> getAllTypes() {
		ServiceResponse<SegmentDTO> response = new ServiceResponse<>();
		try {
			List<SegmentDTO> listOfSegments = segmentationDTORepository.getAllTypeDTO();
			response.setResponseObjects(listOfSegments);
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_CATEGORY.couldNotFind());
		}
		return response;
	}

	@Override
	public ServiceResponse<ProductCategory> getProductCategoryById(long id) {
		ServiceResponse<ProductCategory> response = new ServiceResponse<>();
		try {
			Optional<ProductCategory> productCategory = productCategoryRepository.findById(id);
			if(!productCategory.isPresent()) {
				response.addErrorMessage(ServiceErrorMessages.PRODUCT_CATEGORY.couldNotFind(id));
			}else {
				response.addResponseObject(productCategory.get());
			}
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_CATEGORY.couldNotFind());
		}
		return response;
	}

	@Override
	public ServiceResponse<ProductSubCategory> getProductSubCategoryById(long id) {
		ServiceResponse<ProductSubCategory> response = new ServiceResponse<>();
		try {
			Optional<ProductSubCategory> productSubCategory = productSubCategoryRepository.findById(id);
			if(!productSubCategory.isPresent()) {
				response.addErrorMessage(ServiceErrorMessages.PRODUCT_SUBCATEGORY.couldNotFind(id));
			}else {
				response.addResponseObject(productSubCategory.get());
			}
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_SUBCATEGORY.couldNotFind());
		}
		return response;
	}

	@Override
	public ServiceResponse<ProductType> getProductTypeById(long id) {
		
		
		ServiceResponse<ProductType> response = new ServiceResponse<>();
		try {
			Optional<ProductType> productType = productTypeRepository.findById(id);
			if(!productType.isPresent()) {
				response.addErrorMessage(ServiceErrorMessages.PRODUCT_TYPE.couldNotFind(id));
			}else {
				response.addResponseObject(productType.get());
			}
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_TYPE.couldNotFind());
		}
		return response;
	}

	@Override
	public ServiceResponse<SegmentDTO> getSubCategoriesByCategoryId(long categoryId) {

		
		ServiceResponse<SegmentDTO> response = new ServiceResponse<>();
		try {
			List<SegmentDTO> listOfSegments = categoryId > 0 ? segmentationDTORepository.getAllSubCategoryDTO(categoryId)
					: segmentationDTORepository.getAllSubCategoryDTO();
			response.setResponseObjects(listOfSegments);
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_CATEGORY.couldNotFind());
		}
		return response;
	}

	@Override
	public ServiceResponse<SegmentDTO> getTypesBySubCategoryId(long subCategoryId) {
		
		ServiceResponse<SegmentDTO> response = new ServiceResponse<>();
		try {
			List<SegmentDTO> listOfSegments = subCategoryId > 0 ? segmentationDTORepository.getAllTypeDTO(subCategoryId)
					: segmentationDTORepository.getAllTypeDTO();
			response.setResponseObjects(listOfSegments);
		} catch (Exception e) {
			response.addErrorMessage(ServiceErrorMessages.PRODUCT_CATEGORY.couldNotFind());
		}
		return response;
	}
	
	@Override

	public ServiceResponse<SegmentationModelObject> fullSegmentation(Product currentProduct) {
		ServiceResponse<SegmentDTO> response = new ServiceResponse<>();
		SegmentationModelObject fullSegmentation = new SegmentationModelObject();
		
		long catId = currentProduct.getProductType().getProductSubCategory().getProductCategory().getId();
		long subId = currentProduct.getProductType().getProductSubCategory().getId();
		long typeId = currentProduct.getProductType().getId();
		ProductCategory cat = productCategoryRepository.findById(catId).get();
		ProductSubCategory sub = productSubCategoryRepository.findById(subId).get();
		ProductType type = productTypeRepository.findById(typeId).get();
		SegmentDTO catDTO = new SegmentDTO(cat.getId(), cat.getName());
		SegmentDTO subDTO = new SegmentDTO(sub.getId(), sub.getName());
		SegmentDTO typeDTO = new SegmentDTO(type.getId(), type.getName());
		
		fullSegmentation.setCategories(segmentationDTORepository.getAllCategoryDTO());
		fullSegmentation.setSubCategories(segmentationDTORepository.getAllSubCategoryDTO(catId));
		fullSegmentation.setTypes(segmentationDTORepository.getAllTypeDTO(subId));

		fullSegmentation
				.setSelectedCat(catId);
		
		
		fullSegmentation.setSelectedSub(subId);
		fullSegmentation.setSelectedType(typeId);
		ServiceResponse<SegmentationModelObject> returningResponse = new ServiceResponse<>();
		returningResponse.addResponseObject(fullSegmentation);
		return returningResponse;
	}

	@Override
	public ServiceResponse<Object> prepareSegmentationModel(SegmentationModelObject categoryDTO) {
		ServiceResponse<SegmentDTO> listOfSegmentDTOs = new ServiceResponse<>();
		prepareCategoryModel(categoryDTO);
		
		return new ServiceResponse<>();
	}
	@Override
	public ServiceResponse<Object> prepareProductConfig(SegmentationModelObject categoryDTO, ProductSearchConfig config) {
		config.setProductCategoryId(categoryDTO.getSelectedCat());
		config.setProductSubCategoryId(categoryDTO.getSelectedSub());
		config.setProductTypeId(categoryDTO.getSelectedType());
		
		return new ServiceResponse<>();
	}

	private void prepareCategoryModel(SegmentationModelObject categoryDTO) {
		ServiceResponse<SegmentDTO> listOfSegmentDTOs;
		categoryDTO.setCategories(getAllCategories().getResponseObjects());
		if (categoryDTO.getSelectedCat() > 0) {
			listOfSegmentDTOs = getSubCategoriesByCategoryId(categoryDTO.getSelectedCat());
			if (listOfSegmentDTOs.isSucessful()) {
				categoryDTO.setSubCategories(listOfSegmentDTOs.getResponseObjects());
			}
			if (categoryDTO.getSelectedSub() > 0) {
				listOfSegmentDTOs = getTypesBySubCategoryId(categoryDTO.getSelectedSub());
				if (listOfSegmentDTOs.isSucessful()) {
					categoryDTO.setTypes(listOfSegmentDTOs.getResponseObjects());
				}
			} else {
				categoryDTO.getTypes().clear();
				categoryDTO.setSelectedType(0);
			}
		} else {
			resetCategories(categoryDTO);
		}
	}
	

	private void resetCategories(SegmentationModelObject categoryModelObject) {
		categoryModelObject.setSelectedCat(0);
		categoryModelObject.setSelectedSub(0);
		categoryModelObject.setSelectedType(0);
		ServiceResponse<SegmentDTO> response = getAllCategories();
		if(response.isSucessful())
		categoryModelObject.setCategories(response.getResponseObjects());
		categoryModelObject.getSubCategories().clear();
		categoryModelObject.getTypes().clear();
	}

	@Override
	public ServiceResponse<Integer> create(String name, SegmentationModelObject segmentationModel) {
		ServiceResponse<Integer> response = new ServiceResponse<>();
		try {
			switch(whichNewSegment(segmentationModel)) {
			case 1:
				ProductCategory catResult = productCategoryRepository.save(new ProductCategory(name));
				response.addResponseObject(1);
				break;
			case 2:
				Optional<ProductCategory> persistedProductCategory = productCategoryRepository.findById(segmentationModel.getSelectedCat());
				if(persistedProductCategory.isPresent()) {
					ProductSubCategory subResult = productSubCategoryRepository.save(new ProductSubCategory(name, persistedProductCategory.get()));					
					response.addResponseObject(2);
				}
				break;
			case 3:
				Optional<ProductSubCategory> persistedProductSubCategory = productSubCategoryRepository.findById(segmentationModel.getSelectedSub());
				if(persistedProductSubCategory.isPresent()) {
					ProductType typeResult = productTypeRepository.save(new ProductType(name, persistedProductSubCategory.get()));
					response.addResponseObject(3);
				}
				break;
			}
			return response;
		}
		catch(Exception e) {
			logger.error(e.toString());
			return response;
		}
	}

	private int whichNewSegment(SegmentationModelObject segmentationModel) {
		if(segmentationModel.getSelectedCat() == 0) return 1;
		return segmentationModel.getSelectedSub() == 0 ? 2 : 3;
	}

}
