package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;

import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductSubCategory;
import springWebshop.application.model.domain.ProductType;
import springWebshop.application.model.dto.SegmentDTO;

public interface ProductSegmentationService {
	
	List<SegmentDTO> getAllCategories();
	List<SegmentDTO> getAllSubCategories();
	List<SegmentDTO> getAllTypes();
	List<SegmentDTO> getSubCategoriesByCategoryId(long categoryId);
	List<SegmentDTO> getTypesBySubCategoryId(long subCategoryId);
	
	int getNoCategories();
	int getNoSubCategories();
	int getNoTypes();
	
	ArrayList<ProductType> getTypeStore();
	ArrayList<ProductSubCategory> getSubCategoryStore();
	ArrayList<ProductCategory> getCategoryStore();
	
	

}
