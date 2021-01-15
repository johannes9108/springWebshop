package springWebshop.application.thymeleafControllers;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
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
import springWebshop.application.model.viewModels.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.product.SegmentationService;

@Controller
@RequestMapping("webshop")
@SessionAttributes({ "sessionModel" })
public class ProductController {

	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;
	@Autowired
	@Qualifier("SegmentationServiceImpl")
	SegmentationService productSegmentationService;
	@Autowired
	CustomerRepository customerRepository;
	@ModelAttribute("links")
	private LinkedHashMap<String, String> getLinks() {
		LinkedHashMap<String, String> linkMap = new LinkedHashMap<>();
		linkMap.put("Products", "/webshop/products");
		linkMap.put("Shopping Cart", "/webshop/shoppingcart");
		return linkMap;

	}

	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService, productSegmentationService);
	}

	@GetMapping(path = { "products" })
	public String getAllProducts(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m,
			Authentication authentication) {

		int currentPage = getCurrentPage(session.getProductPage(), pathPage);

		ProductSearchConfig config = prepareProductSearchConfigGet(session);
		prepareDisplayProductsGet(session, m, currentPage, config);
		return "displayProducts";
	}
	
	@PostMapping(path = { "products" })
	public String postProducts(@RequestParam("addToCart") Optional<Integer> productId,
			@ModelAttribute("sessionModel") SessionModel session, Model m,
			@RequestParam("searchInput") Optional<String> searchTerm,
			@RequestParam("reset") Optional<String> reset) {
			
		int currentPage = session.getProductPage();
		ProductSearchConfig config = prepareProductSearchConfigPost(productId, session, searchTerm, reset);

		ServiceResponse<Product> response = productService.getProducts(config, currentPage > 0 ? currentPage - 1 : 0,
				10);
		prepareDisplayProductsPost(session, m, config, response);
		return "displayProducts";
	}

	@GetMapping("/products/product/{id}")
	public String getProduct(Model m, @PathVariable("id") long productId,
			@ModelAttribute("sessionModel") SessionModel session) {
		m.addAttribute("linkMap", getLinks());
		ServiceResponse<Product> response = productService.getProductById(productId);
		if (response.isSucessful()) {
			m.addAttribute("currentProduct", response.getResponseObjects().get(0));
		} else {
			m.addAttribute("errorMessage", response.getErrorMessages());
			return "/webshop/products";
		}
		return "displayProduct";
	}

	@GetMapping("/currentuser")
	public String getLoggeinUser() {
		return "loggedInUser";
	}

	@PostMapping(value = "/products/product/{id}")
	public String postChangeSpecificItemQuantity(Product product,
			@RequestParam(name = "action") Optional<String> action, @PathVariable("id") Optional<Integer> productId,
			@ModelAttribute("sessionModel") SessionModel session, Model m) {
		handleCartActions(action, productId, session);
		m.addAttribute("currentProduct", product);
		m.addAttribute("linkMap", getLinks());

		return "displayProduct";
	}
	
	@GetMapping("shoppingcart")
	public String getShoppingCart(@ModelAttribute SessionModel sesionModel, Model m) {
		System.out.println(sesionModel);
		m.addAttribute("linkMap", getLinks());
		return "displayShoppingCart";
	}

	@PostMapping("shoppingcart")
	public String postShoppingCart(@RequestParam("id") Optional<Integer> productId,
			@RequestParam("action") Optional<String> action, @ModelAttribute SessionModel sesionModel, Model m) {

		if (productId.isPresent()) {
			if (action.isPresent() && action.get().equals("add"))
				sesionModel.getCart().addItem(productId.get());
			else if (action.isPresent() && action.get().equals("remove"))
				sesionModel.getCart().removeItem((productId.get()));
		}

		m.addAttribute("linkMap", getLinks());
		return "displayShoppingCart";
	}

	// Utility Methods
	private void handleCartActions(Optional<String> action, Optional<Integer> productId, SessionModel session) {
		if (action.isPresent()) {
			String stringAction = action.get();
			switch(action.get().toLowerCase()) {
			case "add":
				session.getCart().addItem(productId.get());
				break;
			case "remove":
				session.getCart().removeItem(productId.get());
				break;
			}
		}
	}
	
	private void prepareDisplayProductsPost(SessionModel session, Model m, ProductSearchConfig config,
			ServiceResponse<Product> response) {
		if (response.isSucessful()) {
			m.addAttribute("totalPages", response.getTotalPages());
			m.addAttribute("searchInput", config.getSearchString());
			session.setSearchString(config.getSearchString());

		} else {
			m.addAttribute("errorMessages", response.getErrorMessages());
			m.addAttribute("totalPages", 1);
		}

		m.addAttribute("allProducts", response.getResponseObjects());
		m.addAttribute("linkMap", getLinks());
	}

	private ProductSearchConfig prepareProductSearchConfigPost(Optional<Integer> productId, SessionModel session,
			Optional<String> searchTerm, Optional<String> reset) {
		if(reset.isPresent()) {
			session.setSearchString("");
		}
		
		productSegmentationService.prepareSegmentationModel(session.getCategoryModel());
		ProductSearchConfig config = new ProductSearchConfig();
		config.setPublished(true);
		productSegmentationService.prepareProductConfig(session.getCategoryModel(), config);
		
		if(searchTerm.isPresent())
			config.setSearchString(searchTerm.get());
		else
			config.setSearchString("");
		if (productId.isPresent())
			session.getCart().addItem(productId.get());
		return config;
	}
	private int getCurrentPage(int pageType, Optional<Integer> pathPage) {
		return pathPage.isPresent() ? pathPage.get() : pageType;
	}
	
	private void prepareDisplayProductsGet(SessionModel session, Model m, int currentPage,
			ProductSearchConfig config) {
		ServiceResponse<Product> response = productService.getProducts(config, currentPage > 0 ? currentPage - 1 : 0,
				10);

		if (response.isSucessful()) {
			session.setProductPage(currentPage);
			m.addAttribute("totalPages", response.getTotalPages());
			m.addAttribute("searchInput", config.getSearchString());
			session.setSearchString(config.getSearchString());
			
		} else {
			m.addAttribute("errorMessages", response.getErrorMessages());
			m.addAttribute("totalPages", 1);
			session.setProductPage(1);
		}

		m.addAttribute("allProducts", response.getResponseObjects());
		m.addAttribute("linkMap", getLinks());
		m.addAttribute("sessionModel", session);
	}

	private ProductSearchConfig prepareProductSearchConfigGet(SessionModel session) {
		productSegmentationService.prepareSegmentationModel(session.getCategoryModel());
		ProductSearchConfig config = new ProductSearchConfig();
		config.setPublished(true);

		productSegmentationService.prepareProductConfig(session.getCategoryModel(), config);
		if(session.getSearchString().length()>0) {
			config.setSearchString(session.getSearchString());
		}
		return config;
	}

}
