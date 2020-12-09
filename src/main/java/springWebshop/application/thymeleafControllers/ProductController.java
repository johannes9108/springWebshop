package springWebshop.application.thymeleafControllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.CustomLog;
import springWebshop.application.integration.CustomerRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.dto.CategoryModelObject;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductCategoryService;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductSegmentationService;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.ProductTypeService;

@Controller
@RequestMapping("webshop")
@SessionAttributes({"sessionModel"})
public class ProductController {

	/// ÄNDRADE FRÅN INTERFACE TILL KONKRETE IMPL FÖRATT KOMMA ÅT EGNA Metode
	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;

	@Autowired
	ProductCategoryService productCategoryService;
	
	@Autowired
	@Qualifier("ProductSegmentationServiceImpl")
	ProductSegmentationService productSegmentationService;

	@Autowired
	ProductTypeService productTypeService;

	@Autowired
	CustomerRepository customerRepository;

	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService,productSegmentationService, customerRepository);
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
//
//	@GetMapping()
//	public String home(Model model) {
//		model.addAttribute("newProduct", new ProductFormModel());
//		return "createNewProduct";
//	}
//
//	@PostMapping()
//	public String postHome(ProductFormModel postData, Model model) {
//		System.out.println(postData);
//		model.addAttribute("newProduct", new ProductFormModel());
//		postData.getDomainProduct().setProductType(new ProductType());
//		productService.create(postData.getDomainProduct());
//		return "createNewProduct";
//	}

	@GetMapping(path = { "products","products/{category}","products/{category}/{subcategory}","products/{category}/{subcategory}/{type}" })
	public String getAllProducts(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable(name = "category",required = false) Optional<String> category,
			@PathVariable(name = "subcategory",required = false) Optional<String> subcategory,
			@PathVariable(name = "type",required = false) Optional<String> type,
			@RequestParam(required = false, name = "page",defaultValue = "1") Optional<Integer> pathPage, Model m) {
		System.out.println("GET");
//		resetCategories(session.getCategoryModel());
//		selectFilteredProducts(category,subcategory,type);
		System.out.println("PP:"+pathPage);
		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		ProductSearchConfig config = new ProductSearchConfig();
		handleFiltering(session.getCategoryModel(),config);
		
		ServiceResponse<Product> response = productService.getProducts(config,currentPage > 0 ? currentPage - 1 : 0, 10);
		System.out.println(response);
//		ServiceResponse<Product> response = productService.getProducts(currentPage > 0 ? currentPage - 1 : 0, 10);
		m.addAttribute("allProducts", response.getResponseObjects());
		session.setProductPage(currentPage);
		// Doesnt return Error Message? Empty list
		m.addAttribute("totalPages", response.getTotalPages());
		m.addAttribute("sessionModel", session);

		
		
		
		
		
		
		return "displayProducts";
	}



	private void resetCategories(CategoryModelObject categoryModelObject) {
		categoryModelObject.setSelectedCat(0);
		categoryModelObject.setSelectedSub(0);
		categoryModelObject.setSelectedType(0);
		categoryModelObject.getSubCategories().clear();
		categoryModelObject.getTypes().clear();
	}
	
	private void selectFilteredProducts(Optional<String> category, Optional<String> subcategory, Optional<String> type) {
		System.out.println(category);
		System.out.println(subcategory);
		System.out.println(type);
	}

	@PostMapping(path = { "products" })
	public String postAddItemToCart(
			@RequestParam("id") Optional<Integer> productId,@ModelAttribute("sessionModel") SessionModel session,
			@RequestParam(required = false, name = "page") Optional<Integer> pathPage, Model m) {
//		System.out.println("POST from products");
		ProductSearchConfig config = new ProductSearchConfig();
		handleFiltering(session.getCategoryModel(),config);
		System.out.println("POST");

		
		
		if(productId.isPresent())
			session.getCart().addItem(productId.get());
		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		ServiceResponse<Product> response = productService.getProducts(config,currentPage > 0 ? currentPage - 1 : 0, 10);
		m.addAttribute("currentPage", response.getCurrentPage()-1);
		m.addAttribute("allProducts", response.getResponseObjects());
		// Doesnt return Error Message? Empty list
		
		m.addAttribute("totalPages", response.getTotalPages());

		return "displayProducts";
	}
	
	private void handleFiltering(CategoryModelObject categoryDTO, ProductSearchConfig config) {
		if(categoryDTO.getSelectedCat()>0) {
			categoryDTO.setSubCategories(productSegmentationService.getSubCategoriesByCategoryId(categoryDTO.getSelectedCat()));
			if(categoryDTO.getSelectedSub()>0) {
				categoryDTO.setTypes(productSegmentationService.getTypesBySubCategoryId(categoryDTO.getSelectedSub()));
//				System.out.println(categoryDTO);
			}
			else {
				categoryDTO.getTypes().clear();
				categoryDTO.setSelectedType(0);
			}
		}
		else {
			resetCategories(categoryDTO);
		}
		config.setProductCategoryId(categoryDTO.getSelectedCat());
		config.setProductSubCategoryId(categoryDTO.getSelectedSub());
		config.setProductTypeId(categoryDTO.getSelectedType());
	}

	@GetMapping("/products/product/{id}")
	public String getProduct(Model m,@PathVariable("id") long productId,
			@ModelAttribute("sessionModel") SessionModel session) {
		System.out.println("GetProduct");
		ServiceResponse<Product> response = productService.getProductById(productId);
		m.addAttribute("currentProduct", response.getResponseObjects().get(0));
//		m.addAttribute("quantity", session.getCart().getProductMap())
		return "displayProduct";
	}
	
	@PostMapping(value = "/products/product/{id}",params = "cartAction=Add")
	public String postAddItemToCart(Product product, @PathVariable("id") Optional<Integer> productId,@ModelAttribute("sessionModel") SessionModel session, Model m) {
		if(productId.isPresent())
			session.getCart().addItem(productId.get());
		System.out.println("POST");
		m.addAttribute("currentProduct", product);
		System.out.println(session.getCart());
		return "displayProduct";
	}
	@PostMapping(value = "/products/product/{id}",params = "cartAction=Remove")
	public String postRemoveItemToCart(Product product, @PathVariable("id") Optional<Integer> productId, @ModelAttribute("sessionModel") SessionModel session, Model m) {
		if(productId.isPresent())
		session.getCart().removeItem(productId.get());
		System.out.println("POST");
		m.addAttribute("currentProduct", product);
		System.out.println(session.getCart());
		return "displayProduct";
	}
	
	
	
	@GetMapping("shoppingcart")
	public String getShoppingCart(@ModelAttribute SessionModel sesionModel, Model m) {
		System.out.println(sesionModel);
		return "displayShoppingCart";
	}
	@PostMapping("shoppingcart")
	public String postShoppingCart(
			@RequestParam("id") Optional<Integer> productId,
			@RequestParam("action") Optional<String> action,
			@ModelAttribute SessionModel sesionModel, Model m) {
		
		if(productId.isPresent()) {
			if(action.isPresent() && action.get().equals("add"))
				sesionModel.getCart().addItem(productId.get());
			else if(action.isPresent() && action.get().equals("remove"))
				sesionModel.getCart().removeItem((productId.get()));
		}
		
		
		return "displayShoppingCart";
	}
	

	

//	@PostMapping("products")
//	public String postProduct(Product product, Model m) {
//		System.out.println(product);
//		productService.create(product);
//		m.addAttribute("newProduct", new Product());
//		m.addAttribute("allProducts", productService.getAllProducts(0, 2).getResponseObjects());
//		return "displayProducts";
//	}
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
