package springWebshop.application.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springWebshop.application.integration.product.ProductTypeRepository;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

	@Override
	public boolean save(ProductType productType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<ProductType> getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ProductType> getByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductType> getAllProductTypes() {
		// TODO Auto-generated method stub
		return null;
	}
	

//	@Autowired
//	ProductTypeRepository productTypeRepository;
//
//	@Override
//	public boolean save(ProductSubCategory productSubCategory) {
//		productTypeRepository.save(productSubCategory);
//		return true;
//
//	}
//
//	@Override
//	public Optional<ProductSubCategory> getById(long id) {
//		return productTypeRepository.findById(id);
//	}
//
//	@Override
//	public Optional<ProductSubCategory> getByName(String name) {
//		return productTypeRepository.findByName(name);
//	}
//
//	@Override
//	public List<ProductSubCategory> getAllProductTypes() {
//		return productTypeRepository.findAll();
//	}

}
