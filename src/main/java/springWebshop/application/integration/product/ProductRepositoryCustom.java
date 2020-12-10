package springWebshop.application.integration.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import springWebshop.application.model.domain.Product;
import springWebshop.application.service.product.ProductSearchConfig;


public interface ProductRepositoryCustom {
	
	Page<Product> getProducts(ProductSearchConfig config,int page, int size);

}
