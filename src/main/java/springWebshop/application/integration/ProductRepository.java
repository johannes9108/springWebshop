package springWebshop.application.integration;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import springWebshop.application.model.domain.Product;
import springWebshop.application.service.product.ProductSearchConfig;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    //    List <Product> findByProductTypes_Name(final String typeName);
    List<Product> findByName(String name);
    
//    Page<Product> getProducts(ProductSearchConfig config,int page,int size);
  
}
