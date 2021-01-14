package springWebshop.application.thymeleafControllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import springWebshop.application.model.domain.order.Order.OrderStatus;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.viewModels.AccountDTO;
import springWebshop.application.model.viewModels.SegmentDTO;
import springWebshop.application.model.viewModels.SegmentationModelObject;
import springWebshop.application.model.viewModels.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.admin.AdminService;
import springWebshop.application.service.order.OrderSearchConfig;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.SegmentationService;
import springWebshop.application.service.user.AccountService;

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
	AccountService customerSerivce;

	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;

	@Autowired
	CustomerRepository customerRepository;

	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService, segmentationService);
	}

	@ModelAttribute("links")
	private LinkedHashMap<String, String> getLinks() {
		LinkedHashMap<String, String> linkMap = new LinkedHashMap<>();
		linkMap.put("Products", "/webshop/admin/products");
		linkMap.put("Orders", "/webshop/admin/orders");
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

		int currentPage = getCurrentPage(session.getProductPage(), pathPage);
		ProductSearchConfig config = new ProductSearchConfig();
		if (returnFlag) {
			productsPageWithReturnFlag(session);
		}
		prepareProductsPage(session, m, currentPage, config);

		return "adminViews/adminProductsView";
	}

	@PostMapping(path = { "/products" })
	public String postAllProducts(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage,
			Optional<String> searchInput, Optional<String> newSegmentation,
			@RequestParam(required = false, name = "return", defaultValue = "false") boolean resetFlag, Model m) {
		int currentPage = getCurrentPage(session.getProductPage(), pathPage);
		if (newSegmentation.isPresent()) {
			ServiceResponse<Integer> response = adminService.createNewSegmentation(newSegmentation.get(),
					session.getCategoryModel());
		}
		ProductSearchConfig config = new ProductSearchConfig();
		if (searchInput.isPresent())
			config.setSearchString(searchInput.get());

		prepareProductsPage(session, m, currentPage, config);

		return "adminViews/adminProductsView";
	}

	@GetMapping(path = { "/products/product/{id}" })
	public String getSingleProductEdit(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable("id") long productId,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m) {

		ServiceResponse<Product> response = productService.getProductById(productId);
		if (response.isSucessful()) {
			Product currentProduct = response.getResponseObjects().get(0);
			ServiceResponse<SegmentationModelObject> adminResponse = segmentationService
					.fullSegmentation(currentProduct);
			if (adminResponse.isSucessful()) {
				session.setCategoryModel(adminResponse.getResponseObjects().get(0));
				m.addAttribute("sessionModel", session);
				m.addAttribute("newSegmentation", "");
				m.addAttribute("currentProduct", response.getResponseObjects().get(0));
				return "adminViews/editProduct";
			}

		}
		return "adminViews/adminProductsView";
	}

	@PostMapping(path = { "/products/product/{id}" })
	public String postSingleProductEdit(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@PathVariable("id") long productId,
			@RequestParam(required = false, name = "action") Optional<String> action, Optional<String> newSegmentation,

			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m,
			Product product) {
		ProductSearchConfig config = new ProductSearchConfig();
		handlePostActionsEdit(session, action, newSegmentation, m, product, config);

		ServiceResponse<Product> response = productService.getProductById(productId);
		if (!response.isSucessful()) {
			m.addAttribute("newSegmentation", "");
			m.addAttribute("currentProduct", response.getResponseObjects().get(0));
			m.addAttribute("errorMessage", "Couldn't update product with ID " + productId);
			return "adminViews/adminProductsView";
		}
		m.addAttribute("newSegmentation", "");
		m.addAttribute("currentProduct", response.getResponseObjects().get(0));

		return "adminViews/editProduct";
	}

	@GetMapping("products/new")
	public String getNewProduct(Model m, @ModelAttribute("sessionModel") SessionModel session) {
		m.addAttribute("newProduct", new Product());
		m.addAttribute("newSegmentation", "");

		return "adminViews/newProduct";
	}

	@PostMapping("products/new")
	public String postNewProduct(Product newProduct, @ModelAttribute("sessionModel") SessionModel session,
			@RequestParam(required = false, name = "action") Optional<String> action, Optional<String> newSegmentation,
			@RequestParam(required = false, name = "actionValue") Optional<Long> actionValue, Model m) {
		ProductSearchConfig config = new ProductSearchConfig();
		newProduct = handlePostEventsNew(newProduct, session, action, newSegmentation, m, config);

		m.addAttribute("newProduct", newProduct);
		return "adminViews/newProduct";
	}

	
	@GetMapping(path = { "orders" })
	public String getOrders(Model m, @ModelAttribute("sessionModel") SessionModel session,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage) {

		OrderSearchConfig config = new OrderSearchConfig();
		OrderStatus currentStatus = session.getOrderStatus();
		setStatusInConfig(config, currentStatus);

		int currentPage = getCurrentPage(session.getOrderPage(), pathPage);

		prepareOrdersPage(m, session, config, currentPage);

		return "adminViews/adminOrdersView";
	}

	
	@PostMapping(path = { "orders" })
	public String postOrders(Model m, @ModelAttribute("sessionModel") SessionModel session,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage,
			@RequestParam(required = false, name = "action") Optional<String> action,
			@RequestParam(required = false, name = "actionValue") Optional<String> orderStatus,
			@RequestParam(required = false, name = "expeditedList") Optional<List<String>> expeditedOrders) {
		OrderSearchConfig config = new OrderSearchConfig();
		handlePostActionsOrders(session, action, orderStatus, expeditedOrders, config);
		preparePostOrdersPage(m, session, pathPage, orderStatus, config);

		return "adminViews/adminOrdersView";
	}

	
	@GetMapping("/orders/{id}")
	public String getOrder(@PathVariable("id") Optional<Long> orderId, Model m) {

		ServiceResponse<Order> currentOrder = orderService.getOrderById(orderId.get());
		if (currentOrder.isSucessful()) {
			m.addAttribute("currentOrder", currentOrder.getResponseObjects().get(0));
		} else {
			m.addAttribute("errorMessage", currentOrder.getErrorMessages());
			return "redirect:/webshop/admin/orders";
		}

		return "adminViews/singleOrderView";

	}

	@PostMapping("/orders/{id}")
	public String dispatchOrder(@PathVariable("id") Optional<Long> orderId, Model m) {
		ServiceResponse<Order> currentOrder = orderService.getOrderById(orderId.get());
		if (currentOrder.isSucessful()) {
			ServiceResponse<Order> statusChange = orderService.setStatus(orderId.get(), OrderStatus.DISPATCHED);
			if (statusChange.isSucessful()) {
				m.addAttribute("success", "Order Dispatched");
			} else {
				m.addAttribute("errorMessage", statusChange.getErrorMessages());
			}
			m.addAttribute("currentOrder", currentOrder.getResponseObjects().get(0));
		} else {
			m.addAttribute("errorMessage", currentOrder.getErrorMessages());
			return "redirect:/webshop/admin/orders";
		}

		return "adminViews/singleOrderView";

	}

	@GetMapping(path = { "users" })
	public String getUserForm(Model m) {
		m.addAttribute("newCustomer", new AccountDTO());

		return "adminViews/adminUsersView";
	}

	@PostMapping(path = { "users" })
	public String postUserForm(@Valid AccountDTO newAccount, BindingResult result, Model m) {
		System.out.println(newAccount);
		if (result.hasErrors()) {
			m.addAttribute("newCustomer", newAccount);
			m.addAttribute("errorMessage", "Incorrect Format");
		} else {
			ServiceResponse<Customer> response = customerSerivce.createCustomer(customerAccountDTO(newAccount));
			if (response.isSucessful()) {
				m.addAttribute("success", "New Customer created with ID:" + response.getResponseObjects().get(0));
				m.addAttribute("newCustomer", new AccountDTO());
			} else {
				m.addAttribute("errorMessage", response.getErrorMessages());
				m.addAttribute("newCustomer", newAccount);
			}

		}
		return "adminViews/adminUsersView";
	}

	private void preparePostOrdersPage(Model m, SessionModel session, Optional<Integer> pathPage,
			Optional<String> orderStatus, OrderSearchConfig config) {
		int currentPage = getCurrentPage(session.getOrderPage(), pathPage);
		ServiceResponse<Order> filteredOrders = orderService.getOrders(config, currentPage > 0 ? currentPage - 1 : 0,
				20);

		if (filteredOrders.isSucessful()) {
			if (orderStatus.isPresent()) {
				filteredOrders.getResponseObjects().forEach(t -> System.out.println(t.getId()));
				if (orderStatus.get().compareToIgnoreCase("All") == 0)
					session.setOrderStatus(null);
				else
					session.setOrderStatus(OrderStatus.valueOf(orderStatus.get()));
			}

		} else {
			m.addAttribute("errorMessage", filteredOrders.getErrorMessages());
			session.setOrderPage(1);
		}

		session.setOrderPage(currentPage);
		m.addAttribute("totalPages", filteredOrders.getTotalPages());
		m.addAttribute("orderStatuses", Order.OrderStatus.values());
		m.addAttribute("selectedStatus", session.getOrderStatus() != null ? session.getOrderStatus() : "All");
		m.addAttribute("allOrders", filteredOrders.getResponseObjects());
	}

	private void handlePostActionsOrders(SessionModel session, Optional<String> action, Optional<String> orderStatus,
			Optional<List<String>> expeditedOrders, OrderSearchConfig config) {
		if (action.isPresent()) {
			switch (action.get()) {
			case "filter":
				switch (orderStatus.get()) {
				case "NOT_HANDLED":
					config.setStatus(OrderStatus.NOT_HANDLED);
					break;
				case "DISPATCHED":
					config.setStatus(OrderStatus.DISPATCHED);
					break;
				case "DELIVERY":
					config.setStatus(OrderStatus.DELIVERY);
					break;
				case "DELIVERY_COMPLETED":
					config.setStatus(OrderStatus.DELIVERY_COMPLETED);
					break;
				case "CANCELED":
					config.setStatus(OrderStatus.CANCELED);
					break;
				}
				break;
			case "approve":
				if (expeditedOrders.isPresent()) {

					expeditedOrders.get().forEach(stringOrder -> {
						orderService.setStatus(Long.valueOf(stringOrder), OrderStatus.DISPATCHED);
					});
				}
				config.setStatus(session.getOrderStatus() != null ? session.getOrderStatus() : null);

				break;

			case "disapprove":
				if (expeditedOrders.isPresent()) {
					System.out.println(expeditedOrders + " should be reversed");

					expeditedOrders.get().forEach(stringOrder -> {
						orderService.setStatus(Long.valueOf(stringOrder), OrderStatus.NOT_HANDLED);
					});
				}
				config.setStatus(session.getOrderStatus() != null ? session.getOrderStatus() : null);

				break;
			}

		}
	}

	private void prepareOrdersPage(Model m, SessionModel session, OrderSearchConfig config, int currentPage) {
		ServiceResponse<Order> allOrdersResponse = orderService.getOrders(config, currentPage > 0 ? currentPage - 1 : 0,
				20);
		if (allOrdersResponse.isSucessful()) {

		} else {
			session.setOrderPage(1);
			m.addAttribute("errorMessage", allOrdersResponse.getErrorMessages());
		}
		session.setOrderPage(currentPage);
		m.addAttribute("totalPages", allOrdersResponse.getTotalPages());
		m.addAttribute("orderStatuses", Order.OrderStatus.values());
		m.addAttribute("selectedStatus", session.getOrderStatus() != null ? session.getOrderStatus() : "All");
		m.addAttribute("allOrders", allOrdersResponse.getResponseObjects());
	}

	private void setStatusInConfig(OrderSearchConfig config, OrderStatus currentStatus) {
		if (currentStatus != null) {

			switch (currentStatus) {
			case NOT_HANDLED:
				config.setStatus(OrderStatus.NOT_HANDLED);
				break;
			case DISPATCHED:
				config.setStatus(OrderStatus.DISPATCHED);
				break;
			case DELIVERY:
				config.setStatus(OrderStatus.DELIVERY);
				break;
			case DELIVERY_COMPLETED:
				config.setStatus(OrderStatus.DELIVERY_COMPLETED);
				break;
			case CANCELED:
				config.setStatus(OrderStatus.CANCELED);
				break;
			}

		}
	}

	private Product handlePostEventsNew(Product newProduct, SessionModel session, Optional<String> action,
			Optional<String> newSegmentation, Model m, ProductSearchConfig config) {
		if (newSegmentation.isPresent()) {
			adminService.createNewSegmentation(newSegmentation.get(), session.getCategoryModel());
			segmentationService.prepareSegmentationModel(session.getCategoryModel());
			segmentationService.prepareProductConfig(session.getCategoryModel(), config);
		} else if (action.isPresent()) {
			String ac = action.get();
			if (ac.compareToIgnoreCase("cat") == 0) {
				prepareSegmentationByCategory(session, config);

			} else if (ac.compareToIgnoreCase("sub") == 0) {
				prepareSegmentationBySubCategory(session, config);

			}

		} else {
			if (validateSegmentation(session)) {
				ServiceResponse<Product> response = adminService.createProduct(newProduct, session.getCategoryModel());
				if (response.isSucessful()) {
					m.addAttribute("createdProduct", response.getResponseObjects().get(0));
					m.addAttribute("success", "New Product created");
					newProduct = new Product();
				} else {
					m.addAttribute("errorMessage", response.getErrorMessages());
				}

			} else {
				m.addAttribute("errorMessage", "Product Type must be specified");
			}

		}
		return newProduct;
	}

	
	private void handlePostActionsEdit(SessionModel session, Optional<String> action, Optional<String> newSegmentation,
			Model m, Product product, ProductSearchConfig config) {
		if (newSegmentation.isPresent()) {
			adminService.createNewSegmentation(newSegmentation.get(), session.getCategoryModel());
			segmentationService.prepareSegmentationModel(session.getCategoryModel());
			segmentationService.prepareProductConfig(session.getCategoryModel(), config);
		}

		else if (action.isPresent()) {
			String actionValue = action.get();
			// Handle by a service
			switch (actionValue) {
			case "updateProduct":
				if (validateSegmentation(session)) {
					ServiceResponse<Product> response = adminService.updateProduct(product,
							session.getCategoryModel().getSelectedType());
					if (response.isSucessful()) {
						m.addAttribute("success", "Update Successful");
					} else {
						m.addAttribute("errorMessage", response.getErrorMessages());
					}

				} else {
					m.addAttribute("errorMessage", "Product Type must be specified");
				}
				break;
			case "cat":
				prepareSegmentationByCategory(session, config);
				break;
			case "sub":
				prepareSegmentationBySubCategory(session, config);
				break;
			}

		}
	}

	private void prepareSegmentationBySubCategory(SessionModel session, ProductSearchConfig config) {
		session.getCategoryModel().setSelectedType(0);
		segmentationService.prepareSegmentationModel(session.getCategoryModel());
		segmentationService.prepareProductConfig(session.getCategoryModel(), config);
	}

	private void prepareSegmentationByCategory(SessionModel session, ProductSearchConfig config) {
		session.getCategoryModel().setSelectedSub(0);
		prepareSegmentationBySubCategory(session, config);
	}

	private boolean validateSegmentation(SessionModel session) {
		return session.getCategoryModel().getSelectedCat() > 0 && session.getCategoryModel().getSelectedSub() > 0
				&& session.getCategoryModel().getSelectedType() > 0;
	}

	// UTILITY METHODS
	private Customer customerAccountDTO(@Valid AccountDTO account) {
		Customer c = new Customer(account.getFirstName(), account.getLastName(), account.getPassword(),
				account.getEmail(), account.getPhoneNumber(), account.getMobileNumber(), null);
		return c;
	}

	private void prepareProductsPage(SessionModel session, Model m, int currentPage, ProductSearchConfig config) {
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
		m.addAttribute("totalPages", response.getTotalPages());
		m.addAttribute("searchInput", "");
		m.addAttribute("newSegmentation", "");
		session.setProductPage(currentPage);
	}

	private void productsPageWithReturnFlag(SessionModel session) {
		SegmentationModelObject newSMO = new SegmentationModelObject();
		ServiceResponse<SegmentDTO> listOfCategories = segmentationService.getAllCategories();
		newSMO.setCategories(listOfCategories.getResponseObjects());
		session.setCategoryModel(newSMO);
	}

	private int getCurrentPage(int sessionPage, Optional<Integer> pathPage) {
		int currentPage = pathPage.isPresent() ? pathPage.get() : sessionPage;
		return currentPage;
	}
}
