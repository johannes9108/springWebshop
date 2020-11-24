package springWebshop.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springWebshop.application.integration.ProductCategoryRepository;
import springWebshop.application.model.domain.ProductCategory;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	
	@Autowired
	ProductCategoryRepository productCategoryRepository;
	
	@Override
	public boolean save(ProductCategory productCategory) {
		productCategoryRepository.save(productCategory);
		return true;
	}

	@Override
	public Optional<ProductCategory> getById(long id) {
		return productCategoryRepository.findById(id);
	}

	@Override
	public Optional<ProductCategory> getByName(String name) {
		return productCategoryRepository.findByName(name);
	}

	@Override
	public List<ProductCategory> getAllProductCategories() {
		return productCategoryRepository.findAll();
	}

}
