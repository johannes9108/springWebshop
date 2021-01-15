package springWebshop.application.integration.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    Optional<ProductType> findByName(String name);
    
    
}
