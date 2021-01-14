package springWebshop.application.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import springWebshop.application.integration.account.AccountRepository;
import springWebshop.application.integration.account.AdminRepository;
import springWebshop.application.integration.account.CompanyRepository;
import springWebshop.application.integration.account.CustomerAddressRepository;
import springWebshop.application.integration.account.CustomerRepository;
import springWebshop.application.integration.account.RoleRepository;
import springWebshop.application.integration.order.OrderRepository;
import springWebshop.application.integration.product.ProductCategoryRepository;
import springWebshop.application.integration.product.ProductRepository;
import springWebshop.application.integration.product.ProductSubCategoryRepository;
import springWebshop.application.integration.product.ProductTypeRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.Address;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.order.OrderLine;
import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.domain.user.Admin;
import springWebshop.application.model.domain.user.Company;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.domain.user.ERole;
import springWebshop.application.model.domain.user.Role;
import springWebshop.application.model.viewModels.ShoppingCart;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.user.AccountService;


@Configuration
public class BaseConfig {
    final PasswordEncoder passwordEncoder;
    private final ThymeleafProperties properties;
    @Autowired
    @Qualifier("ProductServiceImpl")
    ProductService productService;
    @Value("${spring.thymeleaf.templates_root:}")
    private String templatesRoot;

    public BaseConfig(ThymeleafProperties properties, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.properties = properties;
    }

    @Bean
    public CommandLineRunner testStuffInHere(ProductRepository productRepository, ProductTypeRepository typeRepo,
                                             ProductCategoryRepository catRepo, ProductSubCategoryRepository subCatRepo,
                                             AccountRepository accountRepository, CompanyRepository companyRepository,
                                             OrderRepository orderRepository, OrderService orderService,
                                             CustomerRepository customerRepository, @Qualifier("ProductServiceImpl") ProductService productService,
                                             AccountService accountService, CustomerAddressRepository addressRespoitory, AdminRepository adminRepository, RoleRepository roleRepository) {

        return (args) -> {
            init(productRepository, typeRepo, catRepo, subCatRepo, companyRepository, orderService, accountService, roleRepository);
        };


    }
    /**
     * Preparing H2 DB for demo purpose
     */
    private void init(ProductRepository productRepository, ProductTypeRepository typeRepo, ProductCategoryRepository catRepo, ProductSubCategoryRepository subCatRepo, CompanyRepository companyRepository, OrderService orderService, AccountService accountService, RoleRepository roleRepository) {
        testingRedesignedProductRepoAndService(productRepository, typeRepo, catRepo, subCatRepo);
        prepareRoles(roleRepository);
        createCustomers(accountService, companyRepository, 10);
        createAdmins(accountService, 2);
        createOrderTest(orderService);
    }

    private void createOrderTest(OrderService orderService) {
        for (int i = 0; i < 50; i++) {
            Address deliveryAddress2 = new CustomerAddress("Storgatan " + (i + 1), randomBetween(11120, 98799), "Stockholm", "Sweden");
            ShoppingCart randomShoppingCart = getRandomShoppingCartDTO(productService, 1, 100);
            ServiceResponse<Order> createOrderResponse = orderService.createOrderFromShoppingCart(randomShoppingCart, randomBetween(1, 10), deliveryAddress2);
            System.out.println("Created order - sucessfully: " + createOrderResponse.isSucessful());
        }

        for (int i = 1; i < 100; i++) {
            ArrayList<Order.OrderStatus> statusList = new ArrayList<>();
            statusList.add(Order.OrderStatus.CANCELED);
            statusList.add(Order.OrderStatus.DISPATCHED);
            statusList.add(Order.OrderStatus.DELIVERY);
            statusList.add(Order.OrderStatus.DELIVERY_COMPLETED);

            if(randomBetween(0,4) != 0){
                ServiceResponse<Order> setStatusResponse = orderService.setStatus(i, statusList.get(randomBetween(0, 3)));
                System.out.println("Changed status successfully: " + setStatusResponse.isSucessful() + setStatusResponse.getErrorMessages());
            }

        }
//        OrderSearchConfig orderSearchConfig = new OrderSearchConfig();
//        orderSearchConfig.setMaxTotalSum(350.00);
//        orderSearchConfig.setSortBy(OrderSearchConfig.SortBy.totalSum);
//        ServiceResponse<Order> searchOrderResponse = orderService.getOrders(orderSearchConfig, 0, 30);
//        System.out.println(searchOrderResponse);
//        searchOrderResponse.getResponseObjects().forEach(order -> {
//            System.out.println(order);
//        });
    }

    private void productSearchTest(ProductService productService) {
        ProductSearchConfig prodConf = new ProductSearchConfig();
        prodConf.setProductCategoryId(0);
        prodConf.setSearchString("9");
        ServiceResponse<Product> prodSearchResp = productService.getProducts(prodConf, 0, 10);
        System.out.println(prodSearchResp);
        prodSearchResp.getResponseObjects().forEach(product -> {
            System.out.println("\n" + product);
        });

        ServiceResponse<Product> prodSearchResp2 = productService.getProducts(prodConf, 1, 10);
        System.out.println(prodSearchResp2);
        prodSearchResp2.getResponseObjects().forEach(product -> {
            System.out.println("\n" + product);
        });
    }

    private void testSetOrderStatus(OrderService orderService, long orderId) {
        System.out.println("Fetch order no 1 ----->" + orderService.getOrderById(orderId).getResponseObjects().get(0));
        ServiceResponse<Order> changeStatusResponse = orderService.setStatus(orderId, Order.OrderStatus.DISPATCHED);
        System.out.println("Change order-status to: Dispatched ----> result: ");
        System.out.println(changeStatusResponse);
        if (!changeStatusResponse.isSucessful())
            throw new RuntimeException("Unexpected result when tresting change of orderStatus in OrderService");
        System.out.println("Updated order ----> " + orderService.getOrderById(orderId).getResponseObjects().get(0));
        ServiceResponse<Order> changeStatusResponse2 = orderService.setStatus(orderId, Order.OrderStatus.NOT_HANDLED);
        System.out.println("Re-change order-status back to: Not Handled ----> result: ");
        System.out.println(changeStatusResponse2);
        if (changeStatusResponse2.isSucessful())
            throw new RuntimeException("Unexpected result when tresting change of orderStatus in OrderService");
        System.out.println("Updated order ----> " + orderService.getOrderById(orderId).getResponseObjects().get(0));
        ServiceResponse<Order> changeStatusResponse3 = orderService.setStatus(orderId, Order.OrderStatus.CANCELED);
        if (!changeStatusResponse3.isSucessful())
            throw new RuntimeException("Unexpected result when tresting change of orderStatus in OrderService");
        System.out.println("Re-change order-status back to: Canceled ----> result: ");
        System.out.println(changeStatusResponse3);
        System.out.println("Updated order ----> " + orderService.getOrderById(orderId).getResponseObjects().get(0));
    }

    private ShoppingCart getRandomShoppingCartDTO(ProductService productService, int productIdFrom, int productIdTo) {
        ShoppingCart shoppingCart = new ShoppingCart(productService);
        for (int i = 0; i < 7; i++) {
            long randomProdId = randomBetween(productIdFrom, productIdTo);
            for (int j = 0; j < randomBetween(1, 3); j++) {
                shoppingCart.addItem(randomProdId);
            }
        }
        return shoppingCart;
    }

    private void prepareRoles(RoleRepository roleRepository) {
        Role role1 = new Role();
        role1.setName(ERole.ADMIN);
        roleRepository.save(role1);
        Role role2 = new Role();
        role2.setName(ERole.CUSTOMER);
        roleRepository.save(role2);
    }

    private void createCustomers(AccountService accountService, CompanyRepository companyRepository, int quantity) {

        for (int i = 0; i < quantity; i++) {
            Customer customer = new Customer();
            customer.setFirstName("Janne");
            customer.setLastName("Larsson");
            customer.setEmail("customer" + (i + 1) + "@gmail.com");
            customer.setPhoneNumber("46709408925");
            customer.setPassword("password");

            for (int j = 0; j < randomBetween(2, 3); j++) {
                CustomerAddress address = new CustomerAddress("Storgatan " + (i + 1), randomBetween(11401, 94789), "City X", "Sweden");
                customer.addAddress(address);
            }
            ServiceResponse<Customer> resp = accountService.createCustomer(customer);
            if (resp.isSucessful()) {
                System.out.println("JAAAAAAAAA!!-->: " + resp);
            } else System.out.println("NEEEEEEEEEEEEEEEEEEEJ!!-->: " + resp);;
        }
        List<Customer> customerList = new ArrayList<>();
        customerList.add(accountService.getCustomerById(1L).getResponseObjects().get(0));
        customerList.add(accountService.getCustomerById(2L).getResponseObjects().get(0));

        Company company = new Company();
        company.setVAT("SE556587983701");
        company.setCustomers(customerList);
        companyRepository.save(company);
    }


    private void createAdmins(AccountService accountService, int quantity) {
        for (int i = 0; i < quantity; i++) {
            Admin admin = new Admin();
            admin.setFirstName("Admin");
            admin.setLastName("Number" + (i + 1));
            admin.setEmail("admin" + (i + 1) + "@gmail.com");
            admin.setPhoneNumber("46709408925");
            admin.setPassword("password");
            accountService.createAdmin(admin);
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
        int noCat = 3, noSub = 4, noType = 5;


        for (int i = 0; i < noCat; i++) {
            ProductCategory category = new ProductCategory("Category " + (i + 1));
            catRepo.save(category);

        }
        for (int i = 0; i < noSub; i++) {
            long rand = new Random().nextInt(noCat) + 1;
//			System.out.println("Cat Rand:"+rand);
            ProductSubCategory subCategory = new ProductSubCategory("SubCategory " + (i + 1),
                    catRepo.findById(rand).get());
            subCatRepo.save(subCategory);
        }
        for (int i = 0; i < noType; i++) {
            long rand = new Random().nextInt(noSub) + 1;
//			System.out.println("Sub Rand:"+rand);
            ProductType prodType = new ProductType("ProductType " + (i + 1),
                    subCatRepo.findById(rand).get());
            typeRepo.save(prodType);

        }

//    	ProductCategory category = new ProductCategory("Möbler");
//
//
//        ProductSubCategory subCategory = new ProductSubCategory("Stol", catRepo.findByName("Möbler").get());
//        subCatRepo.save(subCategory);
//
//        ProductSubCategory subCat2 = new ProductSubCategory();
//        subCat2.setId(1L);
//        ProductType prodType = new ProductType("Gungstol", subCat2);
        String baseImgUrl = "/img/";
        Map<Integer,String> imgMap = new HashMap<>();
        imgMap.put(1, baseImgUrl+"dog.jpg");
        imgMap.put(2, baseImgUrl+"phone.jpg");
        imgMap.put(3, baseImgUrl+"laptop.jpg");
        imgMap.put(4, baseImgUrl+"dogThumbnail.jpg");
        imgMap.put(5, baseImgUrl+"phoneThumbnail.jpg");
        imgMap.put(6, baseImgUrl+"laptopThumbnail.jpg");

        for (int i = 0; i < 40; i++) {
            long rand = new Random().nextInt(noType) + 1;
            Product product1 = new Product();
            product1.setName("Product " + (i + 1));
            product1.setDescription("Testing this big product " + (i + 1));
            product1.setBasePrice(new Random().nextInt(50));
            product1.setProductType(typeRepo.findById(rand).get());
            product1.setVatPercentage(i % 2 == 0 ? 0.25 : 0.12);
            product1.setPublished(true);
            int imgRand = new Random().nextInt(3)+1;
            product1.setFullImageUrl(imgMap.get(imgRand));
            product1.setThumbnailUrl(imgMap.get(imgRand+3));
            
            if (i % 5 == 0) product1.setVatPercentage(0.06);
//            ProductType prodType2 = new ProductType();
//            prodType2.setId(1L);
//            product1.setProductType(prodType2);
            productRepository.save(product1);
            System.out.println(product1.getId() + ":" + product1.getProductType().toString());

//            productRepository.save(product1);
//            boolean result = productService.create(product1).isSucessful();
//            System.out.println((result ? "Successfully created: " : "Error -> Could not create: ") + product1);
        }


//        System.out.println(productService.getAllProducts());

//        System.out.println(productService.getAllProducts());
        System.out.println("första TIO!!!");
//		productService.getAllProducts(0,10).forEach(product -> System.out.println(product.getName()));
        System.out.println("41 - 50!!!");
//		productService.getAllProducts(4,10).forEach(product -> System.out.println(product.getName()));
    }
    /**
     * Hot swap for intelliJ
     */
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