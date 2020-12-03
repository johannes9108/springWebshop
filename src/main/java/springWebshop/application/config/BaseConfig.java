package springWebshop.application.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import springWebshop.application.integration.*;
import springWebshop.application.model.domain.Order;
import springWebshop.application.model.domain.OrderLine;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductSubCategory;
import springWebshop.application.model.domain.ProductType;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductService;

@Configuration
public class BaseConfig {
//    final
//    ProductRepository productRepository;
//    final
//    ProductTypeRepository typeRepo;
//    final
//    ProductCategoryRepository catRepo;
//    final
//    ProductSubCategoryRepository subCatRepo;
//
//    final
//    AccountRepository accountRepository;
//    final
//    CompanyRepository companyRepository;

    //    final
//    ProductSerivce productService;
    private final ThymeleafProperties properties;
    @Autowired
//    @Qualifier("productServiceMockImpl")
            ProductService productService;
    @Value("${spring.thymeleaf.templates_root:}")
    private String templatesRoot;

    public BaseConfig(ThymeleafProperties properties) {
//        this.productRepository = productRepository;
//        this.typeRepo = typeRepo;
//        this.catRepo = catRepo;
//        this.subCatRepo = subCatRepo;
//        this.accountRepository = accountRepository;
//        this.companyRepository = companyRepository;
//        this.productService = productService;
        this.properties = properties;
    }

    @Bean
    public CommandLineRunner testStuffInHere(ProductRepository productRepository, ProductTypeRepository typeRepo,
                                             ProductCategoryRepository catRepo, ProductSubCategoryRepository subCatRepo,
                                             AccountRepository accountRepository, CompanyRepository companyRepository,
                                             OrderRepository orderRepository, OrderService orderService,
                                             CustomerRepository customerRepository, ProductService productService
            , CustomerAddressRespoitory addressRespoitory) {


        return (args) -> {


//        	orderService.getAllOrders().getResponseObjects().forEach(System.out::println);

            testingRedesignedProductRepoAndService(productRepository, typeRepo, catRepo, subCatRepo);
//        	productService.getAllProducts().getResponseObjects().forEach(t->System.out.println(t.getId() + ":" + t.getName()));
//        	System.out.println();
//        	productService.getAllProducts(2, 2).getResponseObjects().forEach(t->System.out.println(t.getId() + ":" + t.getName()));
//        	System.out.println();
//        	System.out.println();
//        	productService.getProductById(1L).getResponseObjects().forEach(t->System.out.println(t.getId() + ":" + t.getName()));
//        	productService.getProductByName("Johannes").getResponseObjects().forEach(t->System.out.println(t.getId() + ":" + t.getName()));
//
            createCustomers(customerRepository, 50);
            Customer persistedCustomer = customerRepository.findById(1L).get();
            System.out.println(persistedCustomer);
            persistedCustomer.getAddresses().forEach(System.out::println);



        };


    }

    private void createCustomers(CustomerRepository customerRepository, int quantity) {
        for (int i = 0; i < quantity; i++) {
            Customer customer = new Customer();
            customer.setFirstName("Janne");
            customer.setLastName("Larsson");
            customer.setEmail("janne.larsson@gmail.com");
            customer.setPhoneNumber("46709408925");

            for (int j = 0; j < randomBetween(2,3); j++) {
                CustomerAddress address = new CustomerAddress("Storgatan " + (i + 1), randomBetween(11401, 94789), "City X", "Sweden");
                customer.addAddress(address);
            }
            customerRepository.save(customer);
        }
    }

    int randomLowerThan(int highest) {
        Random random = new Random();
        return random.nextInt(highest);
    }

    int randomBetween(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private void orderTests(OrderRepository orderRepository, OrderService orderService) {
        for (int i = 0; i < 5; i++) {
            Order localOrder = new Order();
            orderRepository.save(localOrder);
            List<OrderLine> orderlines = new ArrayList<OrderLine>();
            for (int j = 0; j < 10; j++) {
                OrderLine orderLine = new OrderLine();
                orderLine.setDiscount(new Random().nextInt(10));
                orderLine.setSum(new Random().nextInt(10));
                localOrder.addOrderLine(orderLine);
            }
            localOrder.setTotalSum(new Random().nextInt(100));
            localOrder.setTotalVatSum(new Random().nextInt(100));
            orderService.create(localOrder);
        }
    }

    private void testingServiceResponseClass() {
        Product product1 = new Product();
        Product product2 = new Product();
        ArrayList products = new ArrayList();
        ArrayList errors = new ArrayList();
        products.add(product1);
        products.add(product2);
        errors.add(ServiceErrorMessages.PRODUCT.couldNotCreate());
        ServiceResponse response = new ServiceResponse<Product>(products, errors);
        System.out.println(response.isSucessful());
        System.out.println(response.getResponseObjects());
        System.out.println(response.getErrorMessages());
    }

    private void testingRedesignedProductRepoAndService(ProductRepository productRepository,
                                                        ProductTypeRepository typeRepo, ProductCategoryRepository catRepo, ProductSubCategoryRepository subCatRepo) {
        ProductCategory category = new ProductCategory("Möbler");
        catRepo.save(category);

        ProductSubCategory subCategory = new ProductSubCategory("Stol", catRepo.findByName("Möbler").get());
        subCatRepo.save(subCategory);

        ProductSubCategory subCat2 = new ProductSubCategory();
        subCat2.setId(1L);
        ProductType prodType = new ProductType("Gungstol", subCat2);
        typeRepo.save(prodType);

        for (int i = 0; i < 100; i++) {
            Product product1 = new Product();
            product1.setName("Product " + i);
            product1.setDescription("Testing this big product " + i);
            product1.setBasePrice(new Random().nextInt(50));
            ProductType prodType2 = new ProductType();
            prodType2.setId(1L);
            product1.setProductType(prodType2);
            productRepository.save(product1);
        }

        System.out.println(productRepository.findByName(("Product 1")));

//        System.out.println(productService.getAllProducts());

//        System.out.println(productService.getAllProducts());
        System.out.println("första TIO!!!");
//		productService.getAllProducts(0,10).forEach(product -> System.out.println(product.getName()));
        System.out.println("41 - 50!!!");
//		productService.getAllProducts(4,10).forEach(product -> System.out.println(product.getName()));
    }
//
//    private void simpleServiceTest() {
//        ProductCategory existingCategory = new ProductCategory("Alpha");
//        catRepo.save(existingCategory);
//        for (int i = 0; i < 10; i++) {
//            Product product = new Product();
//            product.setName("" + i);
//            product.addProductCategory(new ProductCategory("" + (i - 25)));
//            productService.save(product);
//        }
//
//
//        Optional<Product> product = productService.getProductByName("5");
//        if (product.isPresent()) {
//            System.out.println(product.get());
//            Product localProduct = product.get();
//            localProduct.addProductCategory(new ProductCategory("Beta"));
//            Optional<ProductCategory> exCat = catRepo.findByName("Alpha");
//            if (exCat.isPresent())
//                localProduct.addProductCategory(exCat.get());
//            productService.save(localProduct);
//        }
//        productService.getAllProducts().forEach(p -> System.out.println(p + "\n"));
//    }
//
//    private void companyCustomerTests() {
//        Company company1 = new Company();
//        company1.setName("Ikea");
//        Company company2 = new Company();
//        company2.setName("ABAB");
//        Company company3 = new Company();
//        company3.setName("SWECO");
//
//        Customer customer1 = new Customer();
//        customer1.setFirstName("Johannes");
//        Customer customer2 = new Customer();
//        customer2.setFirstName("Robert");
//        Customer customer3 = new Customer();
//        customer3.setFirstName("Kalle");
//        Customer customer4 = new Customer();
//        customer4.setFirstName("Tess");
//        accountRepository.save(customer4);
//
//        company2.getCustomers().add(customer1);
//        company2.getCustomers().add(customer2);
//        company3.getCustomers().add(customer3);
//
//
//        companyRepository.save(company1);
//        companyRepository.save(company2);
//        companyRepository.save(company3);
//        companyRepository.test().forEach(company -> System.out.println(company + "\n"));
//
//        Optional<Company> oldCompany = companyRepository.findById(3L);
//        if (oldCompany.isPresent()) {
//            companyRepository.delete(oldCompany.get());
//        }
//    }
//
//    private void productTypeCategoryTests() {
//        ProductSubCategory type1 = new ProductSubCategory();
//        type1.setName("Furniture");
//        ProductSubCategory type2 = new ProductSubCategory();
//        type2.setName("Tools");
//        ProductSubCategory type3 = new ProductSubCategory();
//        type3.setName("Grocery");
//        typeRepo.save(type1);
//        typeRepo.save(type2);
//        typeRepo.save(type3);
//
//        ProductCategory cat1 = new ProductCategory();
//        cat1.setName("Sofa");
//        ProductCategory cat2 = new ProductCategory();
//        cat2.setName("Hammer");
//        catRepo.save(cat1);
//        catRepo.save(cat2);
//
//
//        for (int i = 0; i < 100; i++) {
//
//            Product product = new Product();
//            product.setName("Product " + i);
//
//            product.addProductType(i < 51
//                    ? typeRepo.findByName("Furniture").get()
//                    : typeRepo.findByName("Tools").get());
//            product.getProductCategories().add(i < 51
//                    ? catRepo.findByName("Sofa").get()
//                    : catRepo.findByName("Hammer").get());
//
//            if (i % 3 == 0) {
//                product.getProductTypes().add(typeRepo.findById(3L).get());
//            }
////            if (i == 77) product.addProductType(productTypeNuevo);
//
//            productRepository.save(product);
//
//        }
//
//        productRepository.findAll().forEach(product ->
//                System.out.println(product));
//
//        productRepository.findByProductTypes_Name("Grocery").forEach(product ->
//                System.out.println(product.getName()
//                        + product.getProductTypes()
//                        .stream()
//                        .map(ProductSubCategory::getName)
//                        .collect(Collectors.toList())
//                        + product.getProductCategories()
//                        .stream()
//                        .map(ProductCategory::getName)
//                        .collect(Collectors.toList()))
//        );


//    }

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
