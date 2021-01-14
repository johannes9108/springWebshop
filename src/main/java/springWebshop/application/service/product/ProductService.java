package springWebshop.application.service.product;

import springWebshop.application.model.domain.Product;
import springWebshop.application.service.ServiceResponse;

public interface ProductService {
	
	ServiceResponse<Product> getProductById(long id);
	ServiceResponse<Product> getProductByName(String string);
	ServiceResponse<Product> getProducts();
	ServiceResponse<Product> getProducts(int page);
	ServiceResponse<Product> getProducts(int page, int size);
	ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig);
	ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page);
	ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page, int size);
	ServiceResponse<Product> create (Product newProduct);
	ServiceResponse<Product> update(Product updatedProduct);
}
