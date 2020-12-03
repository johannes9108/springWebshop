package springWebshop.application.service.product;

import java.util.List;
import java.util.Optional;

import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductSubCategory;

public interface ProductCategoryService {
	
	boolean save(ProductCategory productCategory);
	Optional<ProductCategory> getById(long id);
	Optional<ProductCategory> getByName(String name);
	List<ProductCategory> getAllProductCategories();
	
	
}
