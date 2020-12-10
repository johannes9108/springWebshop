package springWebshop.application.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.service.ServiceResponse;

@Service("productServiceMockImpl")
public class ProductServiceMockImpl implements ProductService {

	List<Product> productStore;
	static int idProductGenerator = 0;

	@Autowired
	@Qualifier("ProductSegmentationServiceMockImpl")
	SegmentationService productSegmentationService;
	private ArrayList<ProductType> typeStore;
	private ArrayList<ProductCategory> subStore;
	private ArrayList<ProductSubCategory> categoryStore;

//	@PostConstruct
//	void init() {
//		System.out.println(productSegmentationService.getNoTypes());
//		productStore = new ArrayList<Product>();
//		typeStore = productSegmentationService.getTypeStore();
//		subStore = productSegmentationService.getCategoryStore();
//		categoryStore = productSegmentationService.getSubCategoryStore();
//		System.out.println(typeStore);
//		for (int i = 0; i < 100; i++) {
//			Product localProduct = new Product();
//			localProduct.setId(++idProductGenerator);
//			localProduct.setName("Product " + idProductGenerator);
//			localProduct.setBasePrice(new Random().nextInt(1000));
//			localProduct.setProductType(typeStore.get(new Random().nextInt(typeStore.size())));
//			productStore.add(localProduct);
//		}
//	}

	public ProductServiceMockImpl() {

//		productStore.forEach(System.out::println);
//		Product product = new Product();
//		product.setName("Johannes");
//		product.setId(++idProductGenerator);
//		productStore.add(product);
//		product = new Product();
//		product.setName("Hednan");
//		product.setId(++idGenerator);
//		productStore.add(product);
//		product = new Product();
//		productStore.add(product);
	}

	@Override
	public ServiceResponse<Product> getProductById(long id) {
		ArrayList<Product> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		try {
			responseObjects.add(productStore.get(new Long(id - 1).intValue()));

		} catch (IndexOutOfBoundsException e) {
			errorMessages.add("Couldnt find product with id: " + id);
		}
		ServiceResponse<Product> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Product> getProductByName(String string) {
		ArrayList<Product> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		Product localProduct = new Product();
		localProduct.setName(string);

		List<Product> list = productStore.stream().filter(t -> t.getName().compareToIgnoreCase(string) == 0)
				.collect(Collectors.toList());
		responseObjects.addAll(list);

		if (list.size() == 0) {
			errorMessages.add("Couldnt find product with name: " + string);
			System.out.println(errorMessages);

		}

		ServiceResponse<Product> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Product> getProducts() {
		System.out.println("Mock GetAllProducts");
		ArrayList<Product> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		responseObjects.addAll(productStore);
		ServiceResponse<Product> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Product> getProducts(int page, int size) {
		ArrayList<Product> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		responseObjects.addAll(paginatedList(page, size, null));
		ServiceResponse<Product> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	private List<Product> paginatedList(int page, int size, ProductSearchConfig conf) {
		List<Product> localProductStore = null;

		if (conf != null) {
			if (conf.getProductTypeId() > 0) {
				// Filter on Category/SubCategory/Type
//				System.out.println("In type");
				localProductStore = productStore.parallelStream()
						.filter(product -> product.getProductType().getId() == conf.getProductTypeId())
						.collect(Collectors.toList());
			} else if (conf.getProductSubCategoryId() > 0) {
				// Filter on Category/Subcategory

//				System.out.println("In Sub");
				localProductStore = productStore.parallelStream().filter(product -> product.getProductType()
						.getProductSubCategory().getId() == conf.getProductSubCategoryId())
						.collect(Collectors.toList());

//						.filter(subCatId->subCatId==conf.getProductSubCategoryId())
//						.collect(Collectors.toList());
			} else if (conf.getProductCategoryId() > 0) {
//				System.out.println("In cat");
				localProductStore = productStore.parallelStream().filter(p -> p.getProductType().getProductSubCategory()
						.getProductCategory().getId() == conf.getProductCategoryId()).collect(Collectors.toList());

			} else {
				// Check for Tags instead
			}

		}

		return populateResultList(page, size, localProductStore);
	}

	private List<Product> populateResultList(int page, int size, List<Product> filteredList) {
		ArrayList<Product> list = new ArrayList<>();
		if (filteredList == null)
			filteredList = productStore;
		else if (filteredList.size() == 0)
			return list;
		for (int i = page * size; i < filteredList.size() && i < page * size + size; i++) {
			list.add(filteredList.get(i));
		}
		return list;
	}

	@Override
	public ServiceResponse<Product> create(Product newProduct) {
		ArrayList<Product> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		newProduct.setId(++idProductGenerator);
		productStore.add(newProduct);
		ServiceResponse<Product> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Product> update(Product updatedProduct) {
		ArrayList<Product> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		productStore.set(Long.numberOfTrailingZeros(updatedProduct.getId()), updatedProduct);
		ServiceResponse<Product> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Product> getProducts(int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResponse<Product> getProducts(ProductSearchConfig productSearchConfig, int page, int size) {
		System.out.println(productSearchConfig);
		ArrayList<Product> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		responseObjects.addAll(paginatedList(page, size, productSearchConfig));
		ServiceResponse<Product> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

}
