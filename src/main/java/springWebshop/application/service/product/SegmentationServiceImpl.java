package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import springWebshop.application.model.dto.SegmentDTO;
import springWebshop.application.model.dto.SegmentationModelObject;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;

@Service("SegmentationServiceImpl")
public class SegmentationServiceImpl implements SegmentationService {

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

//		return productCategoryRepository.findAll().stream()
//				.map(productCategory -> new SegmentDTO(productCategory.getId(), productCategory.getName()))
//				.collect(Collectors.toList());
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
	public ArrayList<ProductType> getTypeStore() {
		return null;
	}

	@Override
	public ArrayList<ProductSubCategory> getSubCategoryStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ProductCategory> getCategoryStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNoCategories() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNoSubCategories() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNoTypes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServiceResponse<Object> handleFiltering(SegmentationModelObject categoryDTO, ProductSearchConfig config) {
		ServiceResponse<SegmentDTO> listOfSegmentDTOs = new ServiceResponse<>();
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
				System.out.println(categoryDTO);
			} else {
				categoryDTO.getTypes().clear();
				categoryDTO.setSelectedType(0);
			}
		} else {
			resetCategories(categoryDTO);
		}
		config.setProductCategoryId(categoryDTO.getSelectedCat());
		config.setProductSubCategoryId(categoryDTO.getSelectedSub());
		config.setProductTypeId(categoryDTO.getSelectedType());
		
		return new ServiceResponse<>();
	}
	

	private void resetCategories(SegmentationModelObject categoryModelObject) {
		categoryModelObject.setSelectedCat(0);
		categoryModelObject.setSelectedSub(0);
		categoryModelObject.setSelectedType(0);
		categoryModelObject.getSubCategories().clear();
		categoryModelObject.getTypes().clear();
	}

}
