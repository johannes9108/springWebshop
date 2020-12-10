package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.dto.SegmentDTO;

public interface SegmentationService{
	
	List<SegmentDTO> getAllCategories();
	List<SegmentDTO> getAllSubCategories();
	List<SegmentDTO> getAllTypes();
	Optional<ProductCategory> getProductCategoryById(long id);
	Optional<ProductSubCategory> getProductSubCategoryById(long id);
	Optional<ProductType> getProductTypeById(long id);
	List<SegmentDTO> getSubCategoriesByCategoryId(long categoryId);
	List<SegmentDTO> getTypesBySubCategoryId(long subCategoryId);
	
	int getNoCategories();
	int getNoSubCategories();
	int getNoTypes();
	
	ArrayList<ProductType> getTypeStore();
	ArrayList<ProductSubCategory> getSubCategoryStore();
	ArrayList<ProductCategory> getCategoryStore();
	
	

}
