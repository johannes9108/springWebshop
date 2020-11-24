package springWebshop.application.config;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import springWebshop.application.integration.AccountRepository;
import springWebshop.application.integration.CompanyRepository;
import springWebshop.application.integration.ProductCategoryRepository;
import springWebshop.application.integration.ProductRepository;
import springWebshop.application.integration.ProductTypeRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductType;
import springWebshop.application.model.domain.user.Company;
import springWebshop.application.model.domain.user.Customer;

@Configuration
public class BaseConfig {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProductTypeRepository typeRepo;
	@Autowired
	ProductCategoryRepository catRepo;
	
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	CompanyRepository companyRepository;
	
	
	
    @Bean
    public CommandLineRunner testStuffInHere() {

        return (args) -> {
            System.out.println("Put custom code in this method for easy testing capabilities");
            ProductType productTypeNuevo = new ProductType("Typo nuevo");
            productTypeNuevo.setId(0L);

            ProductType type1 = new ProductType();
            type1.setName("Furniture");
            ProductType type2 = new ProductType();
            type2.setName("Tools");
            ProductType type3 = new ProductType();
            type3.setName("Grocery");
            typeRepo.save(type1);
            typeRepo.save(type2);
            typeRepo.save(type3);

            ProductCategory cat1 = new ProductCategory();
            cat1.setName("Sofa");
            ProductCategory cat2 = new ProductCategory();
            cat2.setName("Hammer");
            catRepo.save(cat1);
            catRepo.save(cat2);


            for (int i = 0; i < 100; i++) {

                Product product = new Product();
                product.setTitle("Product " + i);
                productRepository.save(product);
                if (i % 3 == 0) {
                    product.addProductType(typeRepo.findById(3L).get());
                }
                if(i == 77) product.addProductType(productTypeNuevo);
                
                product.addProductType(i < 51
                		? typeRepo.findByName("Furniture")
                				: typeRepo.findByName("Tools"));
                product.getProductCategories().add(i < 51
                		? catRepo.findByName("Sofa")
                				: catRepo.findByName("Hammer"));
                
                if (i % 3 == 0) {
                	product.getProductTypes().add(typeRepo.findById(3L).get());
                }
                if(i == 77) product.addProductType(productTypeNuevo);

                productRepository.save(product);
                
            }

            productRepository.findAll().forEach(product ->
                    System.out.println(product));

            productRepository.findByProductTypes_Name("Grocery").forEach(product ->
                    System.out.println(product.getTitle()
                            + product.getProductTypes()
                            .stream()
                            .map(ProductType::getName)
                            .collect(Collectors.toList())
                            + product.getProductCategories()
                            .stream()
                            .map(ProductCategory::getName)
                            .collect(Collectors.toList()))
            );
            
        	Company company1 = new Company();
        	company1.setName("Ikea");
        	Company company2 = new Company();
        	company2.setName("ABAB");
        	Company company3 = new Company();
        	company3.setName("SWECO");
        	
        	Customer customer1 = new Customer();
        	customer1.setFirstName("Johannes");
        	Customer customer2 = new Customer();
        	customer2.setFirstName("Robert");
        	Customer customer3 = new Customer();
        	customer3.setFirstName("Kalle");
        	Customer customer4 = new Customer();
        	customer4.setFirstName("Tess");
        	accountRepository.save(customer4);
        	
        	company2.getCustomers().add(customer1);
        	company2.getCustomers().add(customer2);
        	company3.getCustomers().add(customer3);
        	
        	
        	
        	
        	companyRepository.save(company1);
        	companyRepository.save(company2);
        	companyRepository.save(company3);
        	companyRepository.test().forEach(company->System.out.println(company + "\n"));
        	
        	Optional<Company> oldCompany = companyRepository.findById(3L);
        	if(oldCompany.isPresent()) {
        		companyRepository.delete(oldCompany.get());
        	}
        	
        	
        	
        	
        	
        };


    }
    
	@Autowired
	private ThymeleafProperties properties;

	@Value("${spring.thymeleaf.templates_root:}")
	private String templatesRoot;
    
    @Bean
	public ITemplateResolver defaultTemplateResolver() {
		FileTemplateResolver resolver = new FileTemplateResolver();
		resolver.setSuffix(properties.getSuffix());
		resolver.setPrefix(templatesRoot);
		resolver.setTemplateMode(properties.getMode());
		resolver.setCacheable(properties.isCache());
		return resolver;
	}


}
