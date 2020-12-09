package springWebshop.application.thymeleafControllers;

import java.util.LinkedHashMap;
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

import springWebshop.application.integration.CustomerRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.dto.CategoryModelObject;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductSegmentationService;
import springWebshop.application.service.product.ProductService;

@Controller
@RequestMapping("webshop/admin")
@SessionAttributes({"sessionModel"})
public class AdminController {
	
	@Autowired
	@Qualifier("ProductSegmentationServiceImpl")
	ProductSegmentationService productSegmentationService;
	
	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService,productSegmentationService, customerRepository);
	}
	
	private LinkedHashMap<String, String> getLinks() {
		LinkedHashMap<String, String> linkMap = new LinkedHashMap<>();
		linkMap.put("Products","/webshop/admin/products");
		linkMap.put("Orders","/webshop/admin/orders");
		linkMap.put("Users","/webshop/admin/users");
		System.out.println("GETLINKS");
		return linkMap;
		
	}
	
	private void handleFiltering(CategoryModelObject categoryDTO, ProductSearchConfig config) {
		if(categoryDTO.getSelectedCat()>0) {
			categoryDTO.setSubCategories(productSegmentationService.getSubCategoriesByCategoryId(categoryDTO.getSelectedCat()));
			if(categoryDTO.getSelectedSub()>0) {
				categoryDTO.setTypes(productSegmentationService.getTypesBySubCategoryId(categoryDTO.getSelectedSub()));
				System.out.println(categoryDTO);
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
	

	private void resetCategories(CategoryModelObject categoryModelObject) {
		categoryModelObject.setSelectedCat(0);
		categoryModelObject.setSelectedSub(0);
		categoryModelObject.setSelectedType(0);
		categoryModelObject.getSubCategories().clear();
		categoryModelObject.getTypes().clear();
	}
	
	@GetMapping(path = {"","/products"})
	public String getAllProducts(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@RequestParam(required = false, name = "page",defaultValue = "1") Optional<Integer> pathPage, Model m) {
		
		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		ProductSearchConfig config = new ProductSearchConfig();
		handleFiltering(session.getCategoryModel(),config);
		
		ServiceResponse<Product> response = productService.getProducts(config,currentPage > 0 ? currentPage - 1 : 0, 20);
		
		m.addAttribute("allProducts", response.getResponseObjects());
		m.addAttribute("totalPages", response.getTotalPages());
		session.setProductPage(currentPage);
		m.addAttribute("linkMap", getLinks());
		return "adminHome";
	}
	@GetMapping(path = {"/products/product/{id}"})
	public String getSingleProductEdit(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable("id") long productId,
			@RequestParam(required = false, name = "page",defaultValue = "1") Optional<Integer> pathPage, Model m) {
		System.out.println("GetProduct");		
		m.addAttribute("linkMap", getLinks());
		ServiceResponse<Product> response = productService.getProductById(productId);
		Product currentProduct = response.getResponseObjects().get(0);
		ProductSearchConfig config = createConfigFromProduct(currentProduct);
		handleFiltering(session.getCategoryModel(),config);
		
		m.addAttribute("fullSegmentation", fullSegmentation());
		m.addAttribute("currentProduct", response.getResponseObjects().get(0));		

		return "editProduct";
	}
	private CategoryModelObject fullSegmentation() {
		
			CategoryModelObject fullSegmentation = new CategoryModelObject();
			fullSegmentation.setCategories(productSegmentationService.getAllCategories());
			fullSegmentation.setSubCategories(productSegmentationService.getAllSubCategories());
			fullSegmentation.setTypes(productSegmentationService.getAllTypes());
		
		return null;
	}

	private ProductSearchConfig createConfigFromProduct(Product currentProduct) {
		ProductSearchConfig conf = new ProductSearchConfig();
		conf.setProductCategoryId(currentProduct.getProductType().getProductSubCategory().getProductCategory().getId());
		conf.setProductCategoryId(currentProduct.getProductType().getProductSubCategory().getId());
		conf.setProductCategoryId(currentProduct.getProductType().getId());
		return conf;
	}

	@PostMapping(path = {"/products/product/{id}"})
	public String postSingleProductEdit(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable("id") long productId,
			@RequestParam(required = false, name = "page",defaultValue = "1") Optional<Integer> pathPage, Model m,
			Product product,CategoryModelObject cateTest
			) {
		System.out.println(product);
		System.out.println("POST Admin Single product");		
		m.addAttribute("linkMap", getLinks());
		ServiceResponse<Product> response = productService.getProductById(productId);
		m.addAttribute("currentProduct", response.getResponseObjects().get(0));		
		
		return "editProduct";
	}
	
	@GetMapping(path = { "orders" })
	public String getAllOrders(Model m) {

		return "adminOrdersView";
	}
	
	@GetMapping(path = { "users" })
	public String getAllUsers( Model m) {

		return "adminUsersView";
	}
	
	
}
