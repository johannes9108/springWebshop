package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springWebshop.application.integration.SegmentationDTORepositoryImpl;
import springWebshop.application.integration.product.ProductCategoryRepository;
import springWebshop.application.integration.product.ProductSubCategoryRepository;
import springWebshop.application.integration.product.ProductTypeRepository;
import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.dto.SegmentDTO;

@Service("ProductSegmentationServiceImpl")
public class SegmentationServiceImpl implements SegmentationService {
	
	@Autowired
	SegmentationDTORepositoryImpl segmentationDTORepository;
	
	@Autowired
	ProductCategoryRepository productCategoryRepository;
	@Autowired
	ProductSubCategoryRepository productSubCategoryRepository;
	@Autowired
	ProductTypeRepository productTypeRepository;

	@Override
	public List<SegmentDTO> getAllCategories() {
		return segmentationDTORepository.getAllCategoryDTO();
//		return productCategoryRepository.findAll().stream()
//				.map(productCategory -> new SegmentDTO(productCategory.getId(), productCategory.getName()))
//				.collect(Collectors.toList());
	}
	
	@Override
	public List<SegmentDTO> getAllSubCategories() {
		return segmentationDTORepository.getAllSubCategoryDTO();
	}

	@Override
	public List<SegmentDTO> getAllTypes() {
		// TODO Auto-generated method stub
		return segmentationDTORepository.getAllTypeDTO();
	}

	@Override
	public Optional<ProductCategory> getProductCategoryById(long id) {
		return productCategoryRepository.findById(id);
	}

	@Override
	public Optional<ProductSubCategory> getProductSubCategoryById(long id) {
		return productSubCategoryRepository.findById(id);
	}

	@Override
	public Optional<ProductType> getProductTypeById(long id) {
		return productTypeRepository.findById(id);
	}
	
	
	
	
	@Override
	public List<SegmentDTO> getSubCategoriesByCategoryId(long categoryId) {
		
		return categoryId>0?segmentationDTORepository.getAllSubCategoryDTO(categoryId):segmentationDTORepository.getAllSubCategoryDTO();
//		return productSubCategoryRepository.findAll().stream()
//				.filter(subCategory -> subCategory.getProductCategory().getId() == categoryId)
//				.map(productSubcategory -> new SegmentDTO(productSubcategory.getId(),
//						productSubcategory.getName()))
//				.collect(Collectors.toList());
	}

	@Override
	public List<SegmentDTO> getTypesBySubCategoryId(long subCategoryId) {
		return subCategoryId>0?segmentationDTORepository.getAllTypeDTO(subCategoryId):segmentationDTORepository.getAllTypeDTO();
//		return productTypeRepository.findAll().stream()
//				.filter(type -> type.getProductSubCategory().getId() == subCategoryId)
//				.map(productType -> new SegmentDTO(productType.getId(), productType.getName()))
//				.collect(Collectors.toList());
	}

//	@Override
//	public int getNoCategories() {
//		return (int) productCategoryRepository.count();
//	}
//
//	@Override
//	public int getNoSubCategories() {
//		return (int) productSubCategoryRepository.count();
//	}
//
//	@Override
//	public int getNoTypes() {
//		return (int) productTypeRepository.count();
//	}

	@Override
	public ArrayList<ProductType> getTypeStore() {
		return null;
	}

	@Override
	public ArrayList<ProductSubCategory> getSubCategoryStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ProductCategory> getCategoryStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNoCategories() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNoSubCategories() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNoTypes() {
		// TODO Auto-generated method stub
		return 0;
	}

	

	

}
