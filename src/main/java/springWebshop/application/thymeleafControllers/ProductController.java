package springWebshop.application.thymeleafControllers;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductType;
import springWebshop.application.model.dto.ProductFormModel;
import springWebshop.application.service.ProductCategoryService;
import springWebshop.application.service.ProductSerivce;
import springWebshop.application.service.ProductTypeService;

@Controller
@RequestMapping("webshop")
public class ProductController {

	@Autowired
	ProductSerivce productService;
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	@Autowired
	ProductTypeService productTypeService;
	
	@ModelAttribute("categories")
	private List<ProductCategory> getAllCategoriesFromService() {
		return productCategoryService.getAllProductCategories();
	}
	@ModelAttribute("types")
	private List<ProductType> getAllTypesFromService() {
		return productTypeService.getAllProductTypes();
	}
	
	
	
	@PostConstruct
	void init() {
		productCategoryService.save(new ProductCategory("Tools"));
		productCategoryService.save(new ProductCategory("Furniture"));
		productCategoryService.save(new ProductCategory("Grocery"));
		
		productTypeService.save(new ProductType("Hammers"));
		productTypeService.save(new ProductType("Couches"));
		productTypeService.save(new ProductType("Ice Cream"));
	}
	
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("newProduct", new ProductFormModel());
		return "createNewProduct";
	}
	
	@PostMapping("/")
	public String postHome(ProductFormModel postData, Model model) {
		System.out.println(postData);
		model.addAttribute("newProduct", new ProductFormModel());
		
		return "createNewProduct";
	}
	
	@PostMapping("/category/newCategory")
	public String postCategory(ProductFormModel postData, Model model) {
		System.out.println("Category:"+postData.getNewCategory());
		productCategoryService.save(new ProductCategory(postData.getNewCategory()));
		model.addAttribute("newProduct", new ProductFormModel());
		return "forward:/webshop/createNewProduct";
	}
	@PostMapping("/type/newType")
	public String postType(ProductFormModel postData, Model model) {
		System.out.println("Type:"+postData.getNewType());
		productTypeService.save(new ProductType(postData.getNewType()));
		model.addAttribute("newProduct", new ProductFormModel());
		return "forward:/webshop/";
	}
	
	
}
