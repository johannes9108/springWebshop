package springWebshop.application.integration.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springWebshop.application.model.domain.segmentation.ProductSubCategory;

import java.util.Optional;

@Repository
public interface ProductSubCategoryRepository extends JpaRepository<ProductSubCategory, Long> {

    Optional<ProductSubCategory> findByName(String name);
    
 
}
