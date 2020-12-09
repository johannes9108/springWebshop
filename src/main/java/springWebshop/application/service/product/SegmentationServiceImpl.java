package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springWebshop.application.integration.SegmentationDTORepositoryImpl;
import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductSubCategory;
import springWebshop.application.model.domain.ProductType;
import springWebshop.application.model.dto.SegmentDTO;

@Service("ProductSegmentationServiceImpl")
public class SegmentationServiceImpl implements ProductSegmentationService {
	
	@Autowired
	SegmentationDTORepositoryImpl segmentationDTORepository;

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
	public List<SegmentDTO> getSubCategoriesByCategoryId(long categoryId) {
		return segmentationDTORepository.getAllSubCategoryDTO(categoryId);
//		return productSubCategoryRepository.findAll().stream()
//				.filter(subCategory -> subCategory.getProductCategory().getId() == categoryId)
//				.map(productSubcategory -> new SegmentDTO(productSubcategory.getId(),
//						productSubcategory.getName()))
//				.collect(Collectors.toList());
	}

	@Override
	public List<SegmentDTO> getTypesBySubCategoryId(long subCategoryId) {
		return segmentationDTORepository.getAllTypeDTO(subCategoryId);
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
