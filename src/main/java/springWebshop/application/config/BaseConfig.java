package springWebshop.application.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import springWebshop.application.integration.AccountRepository;
import springWebshop.application.integration.CompanyRepository;
import springWebshop.application.integration.CustomerAddressRespoitory;
import springWebshop.application.integration.CustomerRepository;
import springWebshop.application.integration.OrderRepository;
import springWebshop.application.integration.ProductCategoryRepository;
import springWebshop.application.integration.ProductRepository;
import springWebshop.application.integration.ProductSubCategoryRepository;
import springWebshop.application.integration.ProductTypeRepository;
import springWebshop.application.model.domain.Address;
import springWebshop.application.model.domain.Order;
import springWebshop.application.model.domain.OrderLine;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.ProductCategory;
import springWebshop.application.model.domain.ProductSubCategory;
import springWebshop.application.model.domain.ProductType;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.dto.ShoppingCartDTO;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderSearchConfig;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductSearchConfig;
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
    @Qualifier("ProductServiceImpl")
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
                                             CustomerRepository customerRepository,@Qualifier("ProductServiceImpl") ProductService productService
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
//            createCustomers(customerRepository, 50);
//            Customer persistedCustomer = customerRepository.findById(1L).get();
//            System.out.println(persistedCustomer);
//            persistedCustomer.getAddresses().forEach(System.out::println);
//
//
//
//            ShoppingCartDTO randomShoppingCartDTO = getRandomShoppingCartDTO(productService, 1, 100);
//            Address deliveryAddress = persistedCustomer.getAddresses()
//                    .stream()
//                    .filter(address -> address.isDefaultAddress())
//                    .findFirst().get();
//
//            System.out.println("Input shoppingCart: \n" + randomShoppingCartDTO
//                    + "\nInput deliveryAddress: \n" + deliveryAddress);
//
//            ServiceResponse<Order> createOrderResponse = orderService.createOrderFromShoppingCart(randomShoppingCartDTO, 1L, deliveryAddress);
//            System.out.println("Created order - response: " + createOrderResponse);
//
//
//
//            if (createOrderResponse.isSucessful()) {
//                System.out.println(createOrderResponse.getResponseObjects());
//            }
//
//            productSearchTest(productService);
//
//
//
//
//
//            testSetOrderStatus(orderService, 1L);
//
//            orderSearchTest(orderService, productService, deliveryAddress, createOrderResponse);




        };


    }

	private void orderSearchTest(OrderService orderService, ProductService productService, Address deliveryAddress,
			ServiceResponse<Order> createOrderResponse) {
		for (int i = 0; i < 100; i++) {
		    ShoppingCartDTO cart = getRandomShoppingCartDTO(productService, 1, 100);

		    orderService.createOrderFromShoppingCart(cart, randomBetween(1,50), deliveryAddress);
		    System.out.println("Created order sucessfully: " + createOrderResponse.isSucessful());
		}

		for (int i = 2; i < 100; i++) {
		    ArrayList<Order.OrderStatus> statusList = new ArrayList<>();
		    statusList.add(Order.OrderStatus.CANCELED);
		    statusList.add(Order.OrderStatus.DISPATCHED);
		    statusList.add(Order.OrderStatus.DELIVERY);
		    statusList.add(Order.OrderStatus.DELIVERY_COMPLETED);

		    ServiceResponse response = orderService.setStatus(i, statusList.get(randomBetween(0,3)));
		    System.out.println("Changed status successfully: " + response.isSucessful() + response.getErrorMessages());
		}
		OrderSearchConfig orderSearchConfig = new OrderSearchConfig();
		orderSearchConfig.setMaxTotalSum(350.00);
		orderSearchConfig.setSortBy(OrderSearchConfig.SortBy.totalSum);
		ServiceResponse<Order> searchOrderResponse = orderService.getOrders(orderSearchConfig, 0,30);
		System.out.println(searchOrderResponse);
		searchOrderResponse.getResponseObjects().forEach(order -> {
		    System.out.println(order);
		});
	}

	private void productSearchTest(ProductService productService) {
		ProductSearchConfig prodConf = new ProductSearchConfig();
		prodConf.setProductCategoryId(0);
		prodConf.setSearchString("9");
		ServiceResponse<Product> prodSearchResp = productService.getProducts(prodConf, 0, 10);
		System.out.println(prodSearchResp);
		prodSearchResp.getResponseObjects().forEach(product -> {
		    System.out.println("\n"+product);
		});

		ServiceResponse<Product> prodSearchResp2 = productService.getProducts(prodConf, 1, 10);
		System.out.println(prodSearchResp2);
		prodSearchResp2.getResponseObjects().forEach(product -> {
		    System.out.println("\n"+product);
		});
	}

    private void testSetOrderStatus(OrderService orderService, long orderId) {
        System.out.println("Fetch order no 1 ----->"+orderService.getOrderById(orderId).getResponseObjects().get(0));
        ServiceResponse<Order> changeStatusResponse = orderService.setStatus(orderId, Order.OrderStatus.DISPATCHED);
        System.out.println("Change order-status to: Dispatched ----> result: ");
        System.out.println(changeStatusResponse);
        if(!changeStatusResponse.isSucessful()) throw new RuntimeException("Unexpected result when tresting change of orderStatus in OrderService");
        System.out.println("Updated order ----> " + orderService.getOrderById(orderId).getResponseObjects().get(0));
        ServiceResponse<Order> changeStatusResponse2 = orderService.setStatus(orderId, Order.OrderStatus.NOT_HANDLED);
        System.out.println("Re-change order-status back to: Not Handled ----> result: ");
        System.out.println(changeStatusResponse2);
        if(changeStatusResponse2.isSucessful()) throw new RuntimeException("Unexpected result when tresting change of orderStatus in OrderService");
        System.out.println("Updated order ----> " + orderService.getOrderById(orderId).getResponseObjects().get(0));
        ServiceResponse<Order> changeStatusResponse3 = orderService.setStatus(orderId, Order.OrderStatus.CANCELED);
        if(!changeStatusResponse3.isSucessful()) throw new RuntimeException("Unexpected result when tresting change of orderStatus in OrderService");
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

    private void createCustomers(CustomerRepository customerRepository, int quantity) {
        for (int i = 0; i < quantity; i++) {
            Customer customer = new Customer();
            customer.setFirstName("Janne");
            customer.setLastName("Larsson");
            customer.setEmail("janne.larsson@gmail.com");
            customer.setPhoneNumber("46709408925");

            for (int j = 0; j < randomBetween(2, 3); j++) {
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
        int noCat = 3, noSub = 4,noType = 5;

		
		for (int i = 0; i < noCat; i++) {
			ProductCategory category = new ProductCategory("Category " + (i + 1));
			catRepo.save(category);

		}
		for (int i = 0; i < noSub; i++) {
			long rand = new Random().nextInt(noCat)+1;
//			System.out.println("Cat Rand:"+rand);
			ProductSubCategory subCategory = new ProductSubCategory("SubCategory " + (i + 1),
					catRepo.findById(rand).get());
			subCatRepo.save(subCategory);
		}
		for (int i = 0; i < noType; i++) {
			long rand = new Random().nextInt(noSub)+1;
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


        for (int i = 0; i < 100; i++) {
        	long rand = new Random().nextInt(noType)+1;
            Product product1 = new Product();
            product1.setName("Product " + (i+1));
            product1.setDescription("Testing this big product " + (i+1));
            product1.setBasePrice(new Random().nextInt(50));
            product1.setProductType(typeRepo.findById(rand).get());
            product1.setVatPercentage(i % 2 == 0 ? 0.25 : 0.12);
            if (i % 5 == 0) product1.setVatPercentage(0.06);
//            ProductType prodType2 = new ProductType();
//            prodType2.setId(1L);
//            product1.setProductType(prodType2);
            productRepository.save(product1);
            System.out.println(product1.getId()+":"+product1.getProductType().toString());

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
