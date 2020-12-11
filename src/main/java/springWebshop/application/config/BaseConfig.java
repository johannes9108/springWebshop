package springWebshop.application.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import springWebshop.application.integration.account.AccountRepository;
import springWebshop.application.integration.account.CompanyRepository;
import springWebshop.application.integration.account.CustomerAddressRespoitory;
import springWebshop.application.integration.account.CustomerRepository;
import springWebshop.application.integration.order.OrderRepository;
import springWebshop.application.integration.product.ProductCategoryRepository;
import springWebshop.application.integration.product.ProductRepository;
import springWebshop.application.integration.product.ProductSubCategoryRepository;
import springWebshop.application.integration.product.ProductTypeRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Address;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.order.OrderLine;
import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.domain.user.*;
import springWebshop.application.model.dto.ShoppingCartDTO;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderSearchConfig;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.user.AccountService;

@Configuration
public class BaseConfig {
    final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
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
                                             CustomerRepository customerRepository, @Qualifier("ProductServiceImpl") ProductService productService
            , CustomerAddressRespoitory addressRespoitory, AdminRepository adminRepository, RoleRepository roleRepository) {


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
<<<<<<< Upstream, based on ExpeditingFromUI



            ShoppingCartDTO randomShoppingCartDTO = getRandomShoppingCartDTO(productService, 1, 100);
            Address deliveryAddress = persistedCustomer.getAddresses()
                    .stream()
                    .filter(address -> address.isDefaultAddress())
                    .findFirst().get();

            System.out.println("Input shoppingCart: \n" + randomShoppingCartDTO
                    + "\nInput deliveryAddress: \n" + deliveryAddress);

            ServiceResponse<Order> createOrderResponse = orderService.createOrderFromShoppingCart(randomShoppingCartDTO, 1L, deliveryAddress);
            System.out.println("Created order - response: " + createOrderResponse);



            if (createOrderResponse.isSucessful()) {
                System.out.println(createOrderResponse.getResponseObjects());
            }

            productSearchTest(productService);





            testSetOrderStatus(orderService, 1L);

            orderSearchTest(orderService, productService, deliveryAddress, createOrderResponse);
=======
            createAdmins(adminRepository, roleRepository, 10);
            Account account = accountRepository.findByEmail("admin1@gmail.com").get();
            System.out.println(account);

            prepareRoles(roleRepository);
            createCustomers(customerRepository, companyRepository, roleRepository, 50);
            Customer persistedCustomer = customerRepository.findById(1L).get();
            System.out.println(persistedCustomer);
            persistedCustomer.getAddresses().forEach(System.out::println);
            createAdmins(adminRepository, roleRepository, 10);
            Account account = accountRepository.findByEmail("admin1@gmail.com").get();
            System.out.println(account);

            createOrderTest(orderService);
            OrderSearchConfig orderConf = OrderSearchConfig.builder()
                    .minTotalSum(10.00)
                    .maxTotalSum(90.00)
                    .status(Order.OrderStatus.NOT_HANDLED)
                    .sortBy(OrderSearchConfig.SortBy.totalSum)
                    .build();
            System.out.println(orderConf);
            ServiceResponse<Order> searchOrderResult = orderService.getOrders(orderConf);
            List<Order> orderList = searchOrderResult.getResponseObjects();
            System.out.println("Start order search result print");
            if (searchOrderResult.isSucessful()){
                orderList.forEach(order -> System.out.println(order.getHeaderString()));
            } else searchOrderResult.getErrorMessages().forEach(System.out::println);

            System.out.println("End order search result print");

            createOrderTest(orderService);
            OrderSearchConfig orderConf = OrderSearchConfig.builder()
                    .minTotalSum(10.00)
                    .maxTotalSum(90.00)
                    .sortBy(OrderSearchConfig.SortBy.totalSum)
                    .build();
            System.out.println(orderConf);
            ServiceResponse<Order> searchOrderResult = orderService.getOrders(orderConf);
            List<Order> orderList = searchOrderResult.getResponseObjects();
            System.out.println("Start order search result print");
            System.out.println(searchOrderResult.isSucessful() ? orderList : searchOrderResult.getErrorMessages());
            System.out.println("End order search result print");

//            OrderSearchConfig orderSearchConfig = new OrderSearchConfig();
//            orderSearchConfig.setMaxTotalSum(350.00);
//            orderSearchConfig.setSortBy(OrderSearchConfig.SortBy.totalSum);
//            ServiceResponse<Order> searchOrderResponse = orderService.getOrders(orderSearchConfig, 0, 30);
//            System.out.println(searchOrderResponse);
//            searchOrderResponse.getResponseObjects().forEach(order -> {
//                System.out.println(order);
//            });
>>>>>>> 5e74aa2 :bug: Fixing a bug in AbstractCustomRepository by adding expected Class as param, example: getPaginatedResult(..., Order.class)

//           


        };


    }

    private void createOrderTest(OrderService orderService) {
        for (int i = 0; i < 100; i++) {
            Address deliveryAddress2 = new CustomerAddress("Storgatan " + (i + 1), randomBetween(11120, 98799), "Stockholm", "Sweden");
            ShoppingCartDTO randomShoppingCartDTO = getRandomShoppingCartDTO(productService, 1, 100);
            ServiceResponse<Order> createOrderResponse = orderService.createOrderFromShoppingCart(randomShoppingCartDTO, randomBetween(1, 50), deliveryAddress2);
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

		    ServiceResponse response = orderService.setStatus(i, statusList.get(randomBetween(0,3)));
		    System.out.println("Changed status successfully: " + response.isSucessful() + response.getErrorMessages());
		}
//		OrderSearchConfig orderSearchConfig = new OrderSearchConfig();
//		orderSearchConfig.setMaxTotalSum(350.00);
//		orderSearchConfig.setSortBy(OrderSearchConfig.SortBy.totalSum);
//		ServiceResponse<Order> searchOrderResponse = orderService.getOrders(orderSearchConfig, 0,30);
//		System.out.println(searchOrderResponse);
//		searchOrderResponse.getResponseObjects().forEach(order -> {
//		    System.out.println(order);
//		});
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
=======
            ServiceResponse<Order> setStatusResponse = orderService.setStatus(i, statusList.get(randomBetween(0, 3)));
            System.out.println("Changed status successfully: " + setStatusResponse.isSucessful() + setStatusResponse.getErrorMessages());
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
>>>>>>> 5e74aa2 :bug: Fixing a bug in AbstractCustomRepository by adding expected Class as param, example: getPaginatedResult(..., Order.class)

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

    private ShoppingCartDTO getRandomShoppingCartDTO(ProductService productService, int productIdFrom, int productIdTo) {
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(productService);
        for (int i = 0; i < 7; i++) {
            long randomProdId = randomBetween(productIdFrom, productIdTo);
            for (int j = 0; j < randomBetween(1, 3); j++) {
                shoppingCartDTO.addItem(randomProdId);
            }
        }
        return shoppingCartDTO;
    }

    private void prepareRoles(RoleRepository roleRepository) {
        Role role1 = new Role();
        role1.setName(ERole.ADMIN);
        roleRepository.save(role1);
        Role role2 = new Role();
        role2.setName(ERole.CUSTOMER);
        roleRepository.save(role2);
    }

    private void createCustomers(CustomerRepository customerRepository, CompanyRepository companyRepository, RoleRepository roleRepository, int quantity) {
        Role customerRole = roleRepository.findByName(ERole.CUSTOMER).get();

        for (int i = 0; i < quantity; i++) {
            Customer customer = new Customer();
            customer.setFirstName("Janne");
            customer.setLastName("Larsson");
            customer.setEmail("customer" + (i + 1) + "@gmail.com");
            customer.setPhoneNumber("46709408925");
            customer.setPassword(bCryptPasswordEncoder.encode("password"));
            HashSet roles = new HashSet();
            roles.add(customerRole);
            customer.setRoles(roles);

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
        customerList.add(accountService.getCustomerById(10L).getResponseObjects().get(0));
        customerList.add(accountService.getCustomerById(20L).getResponseObjects().get(0));

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
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customerRepository.findById(10L).get());
        customerList.add(customerRepository.findById(20L).get());

        Company company = new Company();
        company.setVAT("SE556587983701");
        company.setCustomers(customerList);
        companyRepository.save(company);
    }


    private void createAdmins(AdminRepository adminRepository, RoleRepository roleRepository, int quantity) {
        Role adminRole = roleRepository.findByName(ERole.ADMIN).get();
        for (int i = 0; i < quantity; i++) {
            Admin admin = new Admin();
            admin.setFirstName("Admin");
            admin.setLastName("Number" + (i + 1));
            admin.setEmail("admin" + (i + 1) + "@gmail.com");
            admin.setPhoneNumber("46709408925");
            admin.setPassword(bCryptPasswordEncoder.encode("password"));
            HashSet roles = new HashSet();
            roles.add(adminRole);
            admin.setRoles(roles);
            adminRepository.save(admin);
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

       

		for (int i = 1; i <= noCat; i++) {
			ProductCategory category = new ProductCategory("Category " + (i));
			catRepo.save(category);

		}
		for (int i = 1; i <= noSub; i++) {
			long rand = new Random().nextInt(noCat)+1;
//			System.out.println("Cat Rand:"+rand);
			ProductSubCategory subCategory = new ProductSubCategory("SubCategory " + (i),
					catRepo.findById(rand).get());
			subCatRepo.save(subCategory);
		}
		for (int i = 1; i <= noType; i++) {
			long rand = new Random().nextInt(noSub)+1;
//			System.out.println("Sub Rand:"+rand);
			ProductType prodType = new ProductType("ProductType " + (i),
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


        for (int i = 1; i <= 100; i++) {
            long rand = new Random().nextInt(noType) + 1;
            Product product1 = new Product();
            product1.setName("Product " + (i));
            product1.setDescription("Testing this big product " + (i));
            product1.setBasePrice(new Random().nextInt(50));
            product1.setProductType(typeRepo.findById(rand).get());
            product1.setVatPercentage(i % 2 == 0 ? 0.25 : 0.12);
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
