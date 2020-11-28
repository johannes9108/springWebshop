package springWebshop.application.thymeleafControllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductSubCategory;
import springWebshop.application.model.domain.ProductType;
import springWebshop.application.model.dto.ProductFormModel;
import springWebshop.application.service.ProductCategoryService;
import springWebshop.application.service.ProductService;
import springWebshop.application.service.ProductTypeService;
import springWebshop.application.service.ServiceResponse;

@Controller
@RequestMapping("webshop")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	ProductCategoryService productCategoryService;

	@Autowired
	ProductTypeService productTypeService;

	@ModelAttribute("categories")
	private List<ProductCategory> getAllCategoriesFromService() {
		return productCategoryService.getAllProductCategories();
	}

	@ModelAttribute("types")
	private List<ProductSubCategory> getAllTypesFromService() {
		return productTypeService.getAllProductTypes();
	}

//
//	@PostConstruct
//	void init() {
//		productCategoryService.save(new ProductCategory("Tools"));
//		productCategoryService.save(new ProductCategory("Furniture"));
//		productCategoryService.save(new ProductCategory("Grocery"));
//
//		productTypeService.save(new ProductSubCategory("Hammers"));
//		productTypeService.save(new ProductSubCategory("Couches"));
//		productTypeService.save(new ProductSubCategory("Ice Cream"));*//*
//	}

	@GetMapping()
	public String home(Model model) {
		model.addAttribute("newProduct", new ProductFormModel());
		return "createNewProduct";
	}

	@PostMapping()
	public String postHome(ProductFormModel postData, Model model) {
		System.out.println(postData);
		model.addAttribute("newProduct", new ProductFormModel());
		postData.getDomainProduct().setProductType(new ProductType());
		productService.create(postData.getDomainProduct());
		return "createNewProduct";
	}

	@GetMapping(path = { "products/{page}", "products" })
	public String getAllProducts(@PathVariable(required = false, name = "page") Optional<Integer> pathPage, Model m) {
		m.addAttribute("newProduct", new Product());
		int page = pathPage.isPresent() ? pathPage.get() : 0;

		// Call the service
		ServiceResponse<Product> response = productService.getAllProducts(page > 1 ? page - 1 : 0, 10);
		m.addAttribute("allProducts", response.getResponseObjects());
		System.out.println(response.getErrorMessages());

		m.addAttribute("currentPage", page);

		return "displayProducts";
	}

	@PostMapping("products")
	public String postProduct(Product product, Model m) {
		System.out.println(product);
		productService.create(product);
		m.addAttribute("newProduct", new Product());
		m.addAttribute("allProducts", productService.getAllProducts(0, 2).getResponseObjects());
		return "displayProducts";
	}
//
//	@PostMapping("/category/newCategory")
//	public String postCategory(ProductFormModel postData, Model model) {
//		System.out.println("Category:"+postData.getNewCategory());
//		productCategoryService.save(new ProductCategory(postData.getNewCategory()));
//		model.addAttribute("newProduct", new ProductFormModel());
//		return "forward:/webshop/createNewProduct";
//	}
//	@PostMapping("/type/newType")
//	public String postType(ProductFormModel postData, Model model) {
//		System.out.println("Type:"+postData.getNewType());
//		productTypeService.save(new ProductSubCategory(postData.getNewType()));
//		model.addAttribute("newProduct", new ProductFormModel());
//		return "forward:/webshop/";
//	}

}
