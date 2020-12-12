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
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.dto.SegmentDTO;
import springWebshop.application.model.dto.SegmentationModelObject;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.admin.AdminService;
import springWebshop.application.service.order.OrderSearchConfig;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.SegmentationService;

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
	OrderService orderService;

	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;

	@Autowired
	CustomerRepository customerRepository;

	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService, segmentationService, customerRepository);
	}

	@ModelAttribute("links")
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
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage,
			@RequestParam(required = false, name = "return", defaultValue = "false") boolean returnFlag, Model m) {

		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		ProductSearchConfig config = new ProductSearchConfig();
		if (returnFlag) {
			SegmentationModelObject newSMO = new SegmentationModelObject();
			ServiceResponse<SegmentDTO> listOfCategories = segmentationService.getAllCategories();
			newSMO.setCategories(listOfCategories.getResponseObjects());
			session.setCategoryModel(newSMO);
		}
		segmentationService.prepareSegmentationModel(session.getCategoryModel());
		segmentationService.prepareProductConfig(session.getCategoryModel(), config);
		
		ServiceResponse<Product> response = productService.getProducts(config, currentPage > 0 ? currentPage - 1 : 0,
				20);

		m.addAttribute("allProducts", response.getResponseObjects());
		m.addAttribute("totalPages", response.getTotalPages());
		m.addAttribute("searchInput", "");
		m.addAttribute("newSegmentation", "");
		session.setProductPage(currentPage);
		m.addAttribute("linkMap", getLinks());
		return "adminViews/adminHome";
	}

	@PostMapping(path = { "/products" })
	public String postAllProducts(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage,
			Optional<String> searchInput, Optional<String> newSegmentation,
			@RequestParam(required = false, name = "return", defaultValue = "false") boolean resetFlag, Model m) {
		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		if (newSegmentation.isPresent()) {
			System.out.println("New Segment:" + newSegmentation);
			ServiceResponse<Integer> response = adminService.createNewSegmentation(newSegmentation.get(),
					session.getCategoryModel());
			// TODO DOG SHIT BÖRJAN FÖR ATT AUTO VÄLJA NYA KATEGORIN
//			if(response.isSucessful()) {
//				SegmentationModelObject model = session.getCategoryModel();
//				switch(response.getResponseObjects().get(0)) {
//				case 1:
//					model.setSelectedCat(model.getCategories().size());
//					break;
//				case 2:
//					model.setSelectedSub(model.getSubCategories().size());
//					break;
//				case 3:
//					model.setSelectedType(model.getTypes().size());
//					break;
//				}
//				
//			}
		}
		ProductSearchConfig config = new ProductSearchConfig();
		if (searchInput.isPresent())
			config.setSearchString(searchInput.get());
		if (resetFlag)
			session.setCategoryModel(new SegmentationModelObject());

		segmentationService.prepareSegmentationModel(session.getCategoryModel());
		segmentationService.prepareProductConfig(session.getCategoryModel(), config);
		ServiceResponse<Product> response = productService.getProducts(config, currentPage > 0 ? currentPage - 1 : 0,
				20);
		if (response.isSucessful()) {
			m.addAttribute("allProducts", response.getResponseObjects());
			System.out.println("AllProducts:" + response);
		} else {
			System.out.println(response.getErrorMessages());
		}
		m.addAttribute("searchInput", "");
		m.addAttribute("newSegmentation", "");
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
		ServiceResponse<SegmentationModelObject> adminResponse = segmentationService.fullSegmentation(currentProduct);
		session.setCategoryModel(adminResponse.getResponseObjects().get(0));
		m.addAttribute("sessionModel", session);
//		handleFiltering(session.getCategoryModel(),config);
		m.addAttribute("newSegmentation", "");
		m.addAttribute("currentProduct", response.getResponseObjects().get(0));

		return "adminViews/editProduct";
	}

	@PostMapping(path = { "/products/product/{id}" })
	public String postSingleProductEdit(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable("id") long productId,
			@RequestParam(required = false, name = "action") Optional<String> action, Optional<String> newSegmentation,
			@RequestParam(required = false, name = "actionValue") Optional<Long> actionValue,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m,
			Product product) {
		System.out.println(product);
		System.out.println(action + ":" + actionValue);
		ProductSearchConfig config = new ProductSearchConfig();
		if (newSegmentation.isPresent()) {
			System.out.println("New Segment:" + newSegmentation);
			adminService.createNewSegmentation(newSegmentation.get(), session.getCategoryModel());
			segmentationService.prepareSegmentationModel(session.getCategoryModel());
			segmentationService.prepareProductConfig(session.getCategoryModel(), config);
		}

		else if (action.isPresent()) {
			String ac = action.get();
			// Handle by a service
			if (ac.compareToIgnoreCase("updateProduct") == 0) {

				if (session.getCategoryModel().getSelectedCat() > 0 && session.getCategoryModel().getSelectedSub() > 0
						&& session.getCategoryModel().getSelectedType() > 0) {
					ServiceResponse<Product> response = adminService.updateProduct(product, productId);
					if (response.isSucessful()) {
						product = response.getResponseObjects().get(0);
					} else {
						m.addAttribute("errorMessage", response.getErrorMessages());
					}

				} else {
					m.addAttribute("errorMessage", "Product Type must be specified");
				}

			} else if (ac.compareToIgnoreCase("deleteProduct") == 0) {
				System.out.println("make delete");
				// NOT REQUIRED
			} else if (ac.compareToIgnoreCase("cat") == 0) {
//				session.getCategoryModel().setSubCategories(segmentationService.getSubCategoriesByCategoryId(actionValue.get()).getResponseObjects());
//				session.getCategoryModel().getTypes().clear();
				session.getCategoryModel().setSelectedSub(0);
				session.getCategoryModel().setSelectedType(0);
				segmentationService.prepareSegmentationModel(session.getCategoryModel());
				segmentationService.prepareProductConfig(session.getCategoryModel(), config);

			} else if (ac.compareToIgnoreCase("sub") == 0) {
//				session.getCategoryModel().setTypes(segmentationService.getTypesBySubCategoryId(actionValue.get()).getResponseObjects());
				session.getCategoryModel().setSelectedType(0);
//				session.getCategoryModel().getTypes().clear();
				segmentationService.prepareSegmentationModel(session.getCategoryModel());
				segmentationService.prepareProductConfig(session.getCategoryModel(), config);

			} else if (ac.compareToIgnoreCase("type") == 0) {
				adminService.updateSegmentationOfProduct("type", session.getCategoryModel(), productId);
			} else {

			}
		}
		
		ServiceResponse<Product> response = productService.getProductById(productId);
		if(response.isSucessful()) {
			product = response.getResponseObjects().get(0);
			
		}
		else {
			product = new Product();
			
		}
//		System.out.println("Cat:" + session.getCategoryModel());
//		System.out.println("POST Admin Single product");
		m.addAttribute("linkMap", getLinks());
		m.addAttribute("newSegmentation", "");

		m.addAttribute("currentProduct", product);

		return "adminViews/editProduct";
	}

	@GetMapping("products/new")
	public String getNewProduct(Model m, @ModelAttribute("sessionModel") SessionModel session) {
		m.addAttribute("newProduct", new Product());
		return "adminViews/newProduct";
	}

	@PostMapping("products/new")
	public String postNewProduct(Product newProduct, @ModelAttribute("sessionModel") SessionModel session,
			@RequestParam(required = false, name = "action") Optional<String> action, Optional<String> newSegmentation,
			@RequestParam(required = false, name = "actionValue") Optional<Long> actionValue, Model m) {
		ProductSearchConfig config = new ProductSearchConfig();
		System.out.println("ACTION:" + action);
		if (newSegmentation.isPresent()) {
			System.out.println("New Segment:" + newSegmentation);
			adminService.createNewSegmentation(newSegmentation.get(), session.getCategoryModel());
			segmentationService.prepareSegmentationModel(session.getCategoryModel());
			segmentationService.prepareProductConfig(session.getCategoryModel(), config);
		} else if (action.isPresent()) {
			String ac = action.get();
			if (ac.compareToIgnoreCase("cat") == 0) {
//				session.getCategoryModel().setSubCategories(segmentationService.getSubCategoriesByCategoryId(actionValue.get()).getResponseObjects());
//				session.getCategoryModel().getTypes().clear();
				session.getCategoryModel().setSelectedSub(0);
				session.getCategoryModel().setSelectedType(0);
				segmentationService.prepareSegmentationModel(session.getCategoryModel());
				segmentationService.prepareProductConfig(session.getCategoryModel(), config);

			} else if (ac.compareToIgnoreCase("sub") == 0) {
//				session.getCategoryModel().setTypes(segmentationService.getTypesBySubCategoryId(actionValue.get()).getResponseObjects());
				session.getCategoryModel().setSelectedType(0);
//				session.getCategoryModel().getTypes().clear();
				segmentationService.prepareSegmentationModel(session.getCategoryModel());
				segmentationService.prepareProductConfig(session.getCategoryModel(), config);

			}
		} else {
			if (session.getCategoryModel().getSelectedCat() > 0 && session.getCategoryModel().getSelectedSub() > 0
					&& session.getCategoryModel().getSelectedType() > 0) {
				ServiceResponse<Product> response = adminService.createProduct(newProduct);
				if (response.isSucessful()) {
					m.addAttribute("createdProduct", response.getResponseObjects().get(0));
					newProduct = new Product();
				} else {
					m.addAttribute("errorMessage", response.getErrorMessages());
				}

			} else {
				m.addAttribute("errorMessage", "Product Type must be specified");
			}

		}

		m.addAttribute("newProduct", newProduct);
		return "adminViews/newProduct";
	}

	@GetMapping(path = { "orders" })
	public String getAllOrders(Model m, 
			@ModelAttribute("sessionModel") SessionModel session,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage
			) {
		
		
		OrderSearchConfig config = new OrderSearchConfig();
		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		
		
		
		ServiceResponse<Order> allOrders = orderService.getOrders(config, currentPage > 0 ? currentPage - 1 : 0,
				20);
		
		session.setProductPage(currentPage);
		m.addAttribute("orderStatuses", Order.OrderStatus.values());
		m.addAttribute("selectedStatus",Order.OrderStatus.NOT_HANDLED);
		m.addAttribute("allOrders", allOrders.getResponseObjects());
		
		return "adminViews/adminOrdersView";
	}
	
	@PostMapping(path = { "orders" })
	public String postAllOrders(Model m, 
			@ModelAttribute("sessionModel") SessionModel session,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage,
			@RequestParam(required = false, name="action") Optional<String> action
			) {
		System.out.println(action);
		
		if(action.isPresent()) {
			// Filter out based on input
			
			
		}
		else {
			
		}
		
		
		OrderSearchConfig config = new OrderSearchConfig();
		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		
		
		
		ServiceResponse<Order> allOrders = orderService.getOrders(config, currentPage > 0 ? currentPage - 1 : 0,
				20);
		
		session.setProductPage(currentPage);
		m.addAttribute("orderStatuses", Order.OrderStatus.values());
		m.addAttribute("selectedStatus",Order.OrderStatus.NOT_HANDLED);
		m.addAttribute("allOrders", allOrders.getResponseObjects());
		
		return "adminViews/adminOrdersView";
	}
	
	
	
	

	@GetMapping(path = { "users" })
	public String getAllUsers(Model m) {

		return "adminViews/adminUsersView";
	}

}
