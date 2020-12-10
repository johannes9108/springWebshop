package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.dto.SegmentDTO;

@Service("ProductSegmentationServiceMockImpl")
public class ProductSegmentationServiceMockImpl implements SegmentationService {

	private ArrayList<ProductType> typeStore;
	private ArrayList<ProductSubCategory> subStore;
	private ArrayList<ProductCategory> categoryStore;
	private static int idProductCategoryGenerator;
	private static int idProductSubCategoryGenerator;
	private static int idProductTypeGenerator;
	private int noCat, noSub, noType;

//	ProductCategoryRepository productCategoryRepository;
//	ProductSubCategoryRepository productSubcategoryRepository;
//	ProductTypeRepository productTypeRepository;

//	@PostConstruct
//	void init() {
//		typeStore = new ArrayList<>();
//		subStore = new ArrayList<>();
//		categoryStore = new ArrayList<>();
//
//		this.noCat = 5;
//		this.noSub = 5;
//		this.noType = 10;
//		System.out.println("Init: " + noCat);
//		for (int i = 0; i < noCat; i++) {
//			ProductCategory z = new ProductCategory("ProductCategory " + (i + 1));
//			z.setId(++idProductCategoryGenerator);
//			categoryStore.add(z);
//
//		}
//		for (int i = 0; i < noSub; i++) {
//			ProductSubCategory y = new ProductSubCategory("ProductSubCategory " + (i + 1),
//					categoryStore.get(new Random().nextInt(noCat)));
//			y.setId(++idProductSubCategoryGenerator);
//			subStore.add(y);
//
//		}
//		for (int i = 0; i < noType; i++) {
//			ProductType x = new ProductType("ProductType " + (i + 1), 
//					subStore.get(new Random().nextInt(noSub)));
//			x.setId(++idProductTypeGenerator);
//			typeStore.add(x);
//
//		}
//		categoryStore.forEach(System.out::println);
//		subStore.forEach(System.out::println);
//		typeStore.forEach(System.out::println);
//	
//	}
	public ProductSegmentationServiceMockImpl() {
		
	}

	@Override
	public List<SegmentDTO> getAllCategories() {
		System.out.println("inne  i get all categories");
		return categoryStore.stream()
				.map(productCategory -> new SegmentDTO(productCategory.getId(), productCategory.getName()))
				.collect(Collectors.toList());

	}

	@Override
	public List<SegmentDTO> getSubCategoriesByCategoryId(long categoryId) {
		return subStore.stream()
				.filter(subCategory -> subCategory.getProductCategory().getId() == categoryId)
				.map(productSubcategory -> new SegmentDTO(productSubcategory.getId(),
						productSubcategory.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public List<SegmentDTO> getTypesBySubCategoryId(long subCategoryId) {
		return typeStore.stream()
				.filter(type -> type.getProductSubCategory().getId() == subCategoryId)
				.map(productType -> new SegmentDTO(productType.getId(), productType.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public int getNoCategories() {
		return noCat;
	}

	@Override
	public int getNoSubCategories() {
		return noSub;
	}

	@Override
	public int getNoTypes() {
		return noType;
	}

	@Override
	public ArrayList<ProductType> getTypeStore() {
		return typeStore;
	}

	@Override
	public ArrayList<ProductSubCategory> getSubCategoryStore() {
		return subStore;
	}

	@Override
	public ArrayList<ProductCategory> getCategoryStore() {
		return categoryStore;
	}

	@Override
	public List<SegmentDTO> getAllSubCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SegmentDTO> getAllTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ProductCategory> getProductCategoryById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ProductSubCategory> getProductSubCategoryById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ProductType> getProductTypeById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
