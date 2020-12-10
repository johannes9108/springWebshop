package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import springWebshop.application.integration.product.ProductRepository;
import springWebshop.application.integration.product.ProductTypeRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {

	final ProductRepository productRepository;
	final ProductTypeRepository productTypeRepository;

	final int defaultPageSize = 10;
	final int maxPageSize = 30;

	public ProductServiceImpl(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
		this.productRepository = productRepository;
		this.productTypeRepository = productTypeRepository;
	}

	@Override
	public ServiceResponse<Product> getProductById(long id) {
		ServiceResponse<Product> response = new ServiceResponse<>();
		try {
			Optional<Product> product = productRepository.findById(id);
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
	public ServiceResponse<Product> getProductByName(String name) {
		ServiceResponse<Product> response = new ServiceResponse<>();
		try {
			response.setResponseObjects(productRepository.findByName(name));
		} catch (Exception e) {
			response.addErrorMessage("Problem occurred when searching for product");
		}
		return response;
	}

	@Override
	public ServiceResponse<Product> getProducts() {
		return getAllProductPageAndSize(null,0, defaultPageSize);
	}

	@Override
	public ServiceResponse<Product> getProducts(int page) {
		return getAllProductPageAndSize(null,page, defaultPageSize);
	}
	@Override
	public ServiceResponse<Product> getProducts(int page, int size) {
		return getAllProductPageAndSize(null,page, size);
	}

	
	@Override
	public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig) {
		// TODO Auto-generated method stub
		return getAllProductPageAndSize(productSearchConfig,0, defaultPageSize);
	}

	@Override
	public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page) {
		// TODO Auto-generated method stub
		return getAllProductPageAndSize(productSearchConfig, page, defaultPageSize);
	}

	@Override
	public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page, int size) {
		return getAllProductPageAndSize(productSearchConfig, page, size);
	}

	private ServiceResponse<Product> getAllProductPageAndSize(ProductSearchConfig productSearchConfig,int page, int size) {
		ServiceResponse<Product> serviceResponse = new ServiceResponse<>();
		if (size <= maxPageSize)
			try {
				Page<Product> response = productSearchConfig!= null ? productRepository.getProducts(productSearchConfig, page, size):
					productRepository.findAll(PageRequest.of(page, size));
				setPageMetaData(response, serviceResponse);
				serviceResponse.setResponseObjects(response.getContent());
			} catch (Exception e) {
				serviceResponse.addErrorMessage(ServiceErrorMessages.PRODUCT.couldNotFind() + "s page " + page + ".");
			}
		else
			serviceResponse.addErrorMessage(
					"You have requested " + size + "products. Max allowed page size is " + maxPageSize);
		return serviceResponse;
	}

	private void setPageMetaData(Page page, ServiceResponse serviceResponse) {
		serviceResponse.setTotalPages(page.getTotalPages());
		serviceResponse.setTotalItems((int) page.getTotalElements());
		serviceResponse.setCurrentPage(page.getNumber());
	}

	@Override
	public ServiceResponse<Product> create(Product newProduct) {
		ServiceResponse<Product> response = new ServiceResponse<>();
		List<String> errors = new ArrayList<>();

		if (isValidNewProduct(newProduct, errors))
			try {
				response.addResponseObject(productRepository.save(newProduct));
			} catch (Exception e) {
				response.addErrorMessage(ServiceErrorMessages.PRODUCT.couldNotCreate());
			}
		response.setErrorMessages(errors);
		return response;
	}

	private boolean isValidNewProduct(Product newProduct, List<String> errors) {
		boolean isValid = true;
		if (!isNewProduct(newProduct)) {
			isValid = false;
			errors.add("Product id is provided. Id should not be provided for a new product");
		}
		if (!hasValidProductType(newProduct)) {
			isValid = false;
			errors.add(
					"Provided product type does not exist. New Product must be associated to existing Product Type.");
		}
		return isValid;
	}

	@Override
	public ServiceResponse<Product> update(Product updatedProduct) {
		ServiceResponse<Product> response = new ServiceResponse<>();
		List<String> errors = new ArrayList<>();

		if (isExistingProduct(updatedProduct, errors)) {
			try {
				response.addResponseObject(productRepository.save(updatedProduct));
			} catch (Exception e) {
				response.addErrorMessage(ServiceErrorMessages.PRODUCT.couldNotUpdate(updatedProduct.getId()));
			}
		}
		return response;
	}


	boolean hasValidProductType(Product product) {
		return product.getProductType() != null ? productTypeRepository.existsById(product.getProductType().getId())
				: false;
	}

	boolean isNewProduct(Product product) {
		return product.getId() == 0L ? true : false;
	}

	boolean isExistingProduct(Product product, List<String> errors) {
		boolean isExisting = true;
		if (!productRepository.existsById(product.getId())) {
			isExisting = false;
			errors.add(ServiceErrorMessages.PRODUCT.couldNotFind(product.getId()));
		}
		return isExisting;
	}


}
