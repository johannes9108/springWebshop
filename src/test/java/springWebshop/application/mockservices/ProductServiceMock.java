package springWebshop.application.mockservices;

import springWebshop.application.model.domain.Product;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class ProductServiceMock implements ProductService {
    HashSet<Product> productDb = new HashSet();

    public void loadFakeDb(List<Product> products){
        products.forEach(product -> productDb.add(product));
    }

    @Override
    public ServiceResponse<Product> getProductById(long id) {
        ServiceResponse<Product> response = new ServiceResponse<>();
        try {
            Optional<Product> product = productDb.stream().filter(fakeDbProd -> fakeDbProd.getId() == id).findFirst();
            if (!product.isPresent()) {
                response.addErrorMessage(ServiceErrorMessages.PRODUCT.couldNotFind(id));
            } else {
                response.addResponseObject(product.get());
            }
        } catch (Exception e) {
            response.addErrorMessage(ServiceErrorMessages.PRODUCT.couldNotFind(id));
        }
        return response;
    }

    @Override
    public ServiceResponse<Product> getProductByName(String string) {
        return null;
    }

    @Override
    public ServiceResponse<Product> getProducts() {
        return null;
    }

    @Override
    public ServiceResponse<Product> getProducts(int page) {
        return null;
    }

    @Override
    public ServiceResponse<Product> getProducts(int page, int size) {
        return null;
    }

    @Override
    public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig) {
        return null;
    }

    @Override
    public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page) {
        return null;
    }

    @Override
    public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page, int size) {
        return null;
    }

    @Override
    public ServiceResponse<Product> create(Product newProduct) {
        return null;
    }

    @Override
    public ServiceResponse<Product> update(Product updatedProduct) {
        return null;
    }
}
