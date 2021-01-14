package springWebshop.application.integration.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springWebshop.application.model.domain.segmentation.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

	
    Optional<ProductCategory> findByName(String name);
    
}
