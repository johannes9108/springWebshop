package springWebshop.application.service.product;

import java.util.ArrayList;

import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.dto.SegmentDTO;
import springWebshop.application.model.dto.SegmentationModelObject;
import springWebshop.application.service.ServiceResponse;

public interface SegmentationService{
	
	ServiceResponse<SegmentDTO> getAllCategories();
	ServiceResponse<SegmentDTO> getAllSubCategories();
	ServiceResponse<SegmentDTO> getAllTypes();
	ServiceResponse<ProductCategory> getProductCategoryById(long id);
	ServiceResponse<ProductSubCategory>getProductSubCategoryById(long id);
	ServiceResponse<ProductType> getProductTypeById(long id);
	ServiceResponse<SegmentDTO> getSubCategoriesByCategoryId(long categoryId);
	ServiceResponse<SegmentDTO> getTypesBySubCategoryId(long subCategoryId);
	ServiceResponse<Object> handleFiltering(SegmentationModelObject categoryModel, ProductSearchConfig config);
	
	int getNoCategories();
	int getNoSubCategories();
	int getNoTypes();
	
	ArrayList<ProductType> getTypeStore();
	ArrayList<ProductSubCategory> getSubCategoryStore();
	ArrayList<ProductCategory> getCategoryStore();
	
	

}
