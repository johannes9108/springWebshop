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

import springWebshop.application.integration.account.CustomerRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.dto.SegmentationModelObject;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.admin.AdminService;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.SegmentationService;
import springWebshop.application.service.product.ProductService;

@Controller
@RequestMapping("webshop/admin")
@SessionAttributes({ "sessionModel" })
public class AdminController {

	@Autowired
	@Qualifier("SegmentationServiceImpl")
	SegmentationService segmentationService;
	
	@Autowired
	AdminService adminService;

	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;

	@Autowired
	CustomerRepository customerRepository;

	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService, segmentationService, customerRepository);
	}

	private LinkedHashMap<String, String> getLinks() {
		LinkedHashMap<String, String> linkMap = new LinkedHashMap<>();
		linkMap.put("Products", "/webshop/admin/products");
		linkMap.put("Orders", "/webshop/admin/orders");
		linkMap.put("Users", "/webshop/admin/users");
		return linkMap;

	}

	

	@GetMapping("")
	public String forwardToProducts() {
		return "redirect:/webshop/admin/products";
	}

	@GetMapping(path = { "/products" })
	public String getAllProducts(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m) {
		
		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		ProductSearchConfig config = new ProductSearchConfig();
		segmentationService.handleFiltering(session.getCategoryModel(), config);

		ServiceResponse<Product> response = productService.getProducts(config, currentPage > 0 ? currentPage - 1 : 0,
				20);
		
		m.addAttribute("allProducts", response.getResponseObjects());
		m.addAttribute("totalPages", response.getTotalPages());
		session.setProductPage(currentPage);
		m.addAttribute("linkMap", getLinks());
		return "adminViews/adminHome";
	}

	@GetMapping(path = { "/products/product/{id}" })
	public String getSingleProductEdit(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable("id") long productId,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m) {
		System.out.println("GetProduct");
		m.addAttribute("linkMap", getLinks());
		ServiceResponse<Product> response = productService.getProductById(productId);
		Product currentProduct = response.getResponseObjects().get(0);
		ServiceResponse<SegmentationModelObject> adminResponse = adminService.fullSegmentation(currentProduct);
		session.setCategoryModel(adminResponse.getResponseObjects().get(0));
		m.addAttribute("sessionModel", session);
//		handleFiltering(session.getCategoryModel(),config);

		m.addAttribute("currentProduct", response.getResponseObjects().get(0));

		return "adminViews/editProduct";
	}

	@PostMapping(path = { "/products/product/{id}" })
	public String postSingleProductEdit(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable("id") long productId, @RequestParam(required = false, name = "action") Optional<String> action,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m,
			Product product) {
		System.out.println(product);
		System.out.println(action);
		if (action.isPresent())
		{
			// Handle by a service
			if(action.get().compareToIgnoreCase("updateProduct")==0) {
				adminService.updateProduct(product, productId);
			}
			else if(action.get().compareToIgnoreCase("deleteProduct")==0){
				System.out.println("make delete");
				Product p = productService.getProductById(productId).getResponseObjects().get(0);
				//TODO Create Delete in service
				
				
			}
		}
			//			updateSegmentationOfProduct(action.get(), session.getCategoryModel(), product.getId());

		System.out.println("Cat:" + session.getCategoryModel());
		System.out.println("POST Admin Single product");
		m.addAttribute("linkMap", getLinks());
		
		ServiceResponse<Product> response = productService.getProductById(productId);
		Product currentProduct = response.getResponseObjects().get(0);
		m.addAttribute("currentProduct", response.getResponseObjects().get(0));

		
		ServiceResponse<SegmentationModelObject> adminResponse = adminService.fullSegmentation(currentProduct);
		m.addAttribute("fullSegmentation", adminResponse.getResponseObjects().get(0));
		
		
		return "adminViews/editProduct";
	}


	@GetMapping(path = { "orders" })
	public String getAllOrders(Model m) {

		return "adminViews/adminOrdersView";
	}

	@GetMapping(path = { "users" })
	public String getAllUsers(Model m) {

		return "adminViews/adminUsersView";
	}

}
