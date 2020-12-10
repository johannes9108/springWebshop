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
import springWebshop.application.model.dto.CategoryModelObject;
import springWebshop.application.model.dto.SessionModel;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.SegmentationService;
import springWebshop.application.service.product.ProductService;

@Controller
@RequestMapping("webshop/admin")
@SessionAttributes({ "sessionModel" })
public class AdminController {

	@Autowired
	@Qualifier("ProductSegmentationServiceImpl")
	SegmentationService productSegmentationService;

	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;

	@Autowired
	CustomerRepository customerRepository;

	@ModelAttribute("sessionModel")
	private SessionModel getSessionModel() {
		return new SessionModel(productService, productSegmentationService, customerRepository);
	}

	private LinkedHashMap<String, String> getLinks() {
		LinkedHashMap<String, String> linkMap = new LinkedHashMap<>();
		linkMap.put("Products", "/webshop/admin/products");
		linkMap.put("Orders", "/webshop/admin/orders");
		linkMap.put("Users", "/webshop/admin/users");
		return linkMap;

	}

	private void handleFiltering(CategoryModelObject categoryDTO, ProductSearchConfig config) {
		if (categoryDTO.getSelectedCat() > 0) {
			categoryDTO.setSubCategories(
					productSegmentationService.getSubCategoriesByCategoryId(categoryDTO.getSelectedCat()));
			if (categoryDTO.getSelectedSub() > 0) {
				categoryDTO.setTypes(productSegmentationService.getTypesBySubCategoryId(categoryDTO.getSelectedSub()));
				System.out.println(categoryDTO);
			} else {
				categoryDTO.getTypes().clear();
				categoryDTO.setSelectedType(0);
			}
		} else {
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

	@GetMapping("")
	public String forwardToProducts() {
		return "redirect:/webshop/admin/products";
	}

	@GetMapping(path = { "/products" })
	public String getAllProducts(@ModelAttribute("sessionModel") SessionModel session, BindingResult result,
			@RequestParam(required = false, name = "page", defaultValue = "1") Optional<Integer> pathPage, Model m) {

		int currentPage = pathPage.isPresent() ? pathPage.get() : session.getProductPage();
		ProductSearchConfig config = new ProductSearchConfig();
		handleFiltering(session.getCategoryModel(), config);

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
		session.setCategoryModel(fullSegmentation(currentProduct));
		m.addAttribute("sessionModel", session);
//		handleFiltering(session.getCategoryModel(),config);

		m.addAttribute("currentProduct", response.getResponseObjects().get(0));

		return "adminViews/editProduct";
	}

	private CategoryModelObject fullSegmentation(Product currentProduct) {

		CategoryModelObject fullSegmentation = new CategoryModelObject();
		fullSegmentation.setCategories(productSegmentationService.getAllCategories());
		fullSegmentation.setSubCategories(productSegmentationService.getAllSubCategories());
		fullSegmentation.setTypes(productSegmentationService.getAllTypes());

		fullSegmentation
				.setSelectedCat(currentProduct.getProductType().getProductSubCategory().getProductCategory().getId());
		fullSegmentation.setSelectedSub(currentProduct.getProductType().getProductSubCategory().getId());
		fullSegmentation.setSelectedType(currentProduct.getProductType().getId());

		return fullSegmentation;
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
				Product p = productService.getProductById(productId).getResponseObjects().get(0);
				p = product;
				p.setProductType(productSegmentationService.getProductTypeById(session.getCategoryModel().getSelectedType()).get());
				productService.update(p);
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
		m.addAttribute("fullSegmentation", fullSegmentation(currentProduct));
		return "adminViews/editProduct";
	}

	private void updateSegmentationOfProduct(String action, CategoryModelObject categoryModelObject, long id) {
		ServiceResponse<Product> response = productService.getProductById(id);
		if (response.isSucessful()) {
			Product product = response.getResponseObjects().get(0);
			switch (action) {
			case "type":
				// TODO NOT WORKING WHEN SELECTING 0/ALL
				// TODO EXTRACT ALL THIS LOGIC TO SERVICE CLASSES AS WELL IN THE PRODUCT
				// CONTROLLER
				long newType = categoryModelObject.getSelectedType();

				product.updateProductType(productSegmentationService.getProductTypeById(newType == 0 ? 1 : newType).get());
				productService.update(product);

				break;
			case "sub":
				long newSub = categoryModelObject.getSelectedType();

				product.updateProductSub(productSegmentationService.getProductSubCategoryById(newSub == 0 ? 1 : newSub).get());
				productService.update(product);
				break;
			case "cat":

				break;
			default:
				break;
			}

		}

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
