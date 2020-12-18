package springWebshop.application.restControllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.order.Order.OrderStatus;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.order.OrderSearchConfig;
import springWebshop.application.service.order.OrderService;
import springWebshop.application.service.product.ProductSearchConfig;
import springWebshop.application.service.product.ProductService;
import springWebshop.application.service.user.AccountService;

@RestController
@RequestMapping("webshop/api")
public class BaseRestController {

	@Autowired
	@Qualifier("ProductServiceImpl")
	ProductService productService;

	@Autowired
	@Qualifier("PROD")
	OrderService orderSerivce;

	@Autowired
	AccountService accountService;

	@GetMapping(value = { "products", "products/{page}" })
	public ResponseEntity<List<Product>> getAllProducuts(
			@RequestParam(name = "categoryFilter", required = false, defaultValue = "0") Optional<Long> selectedCat,
			@RequestParam(name = "subCategoryFilter", required = false, defaultValue = "0") Optional<Long> selectedSub,
			@RequestParam(name = "typeFilter", required = false, defaultValue = "0") Optional<Long> selectedType,
			@RequestParam(name = "searchString", required = false, defaultValue = "") Optional<String> searchString,
			@RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size,
			@PathVariable(name = "page", required = false) Optional<Integer> page) {
		int pageToFetch = page.isPresent() && page.get() > 0 ? page.get() - 1 : 0;
		System.out.println(page);
		ProductSearchConfig config = new ProductSearchConfig();
		config.setProductCategoryId(selectedCat.get());
		config.setProductSubCategoryId(selectedSub.get());
		config.setProductTypeId(selectedType.get());
		config.setSearchString(searchString.get());
		ServiceResponse<Product> productResponse = productService.getProducts(config, pageToFetch, size.get());
		if (productResponse.isSucessful()) {
			return ResponseEntity.ok(productResponse.getResponseObjects());
		} else {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}

	@GetMapping(value = { "products/product/{productId}" })
	public ResponseEntity<Product> getSingleProduct(
			@PathVariable(name = "productId", required = false) Optional<Integer> productId) {
		if (productId.isPresent()) {
			ServiceResponse<Product> productResponse = productService.getProductById(productId.get());

			if (productResponse.isSucessful()) {
				return ResponseEntity.ok(productResponse.getResponseObjects().get(0));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PostMapping(value = { "products/new" })
	public ResponseEntity<Product> postSingleProduct(Optional<Product> product) {
		System.out.println(product);
		if (product.isPresent()) {
			ServiceResponse<Product> productResponse = productService.create(product.get());

			if (productResponse.isSucessful()) {
				return ResponseEntity.status(HttpStatus.CREATED).body(productResponse.getResponseObjects().get(0));
			} else {
				System.out.println(productResponse.getErrorMessages());
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping(value = { "products/product/{productId}" })
	public ResponseEntity<Product> putSingleProduct(
			@PathVariable(name = "productId", required = false) Optional<Integer> productId,
			Optional<Product> product) {
		System.out.println(product);
		if (product.isPresent() && productId.isPresent()) {
			product.get().setId(productId.get());
			ServiceResponse<Product> productResponse = productService.update(product.get());

			if (productResponse.isSucessful()) {
				return ResponseEntity.status(HttpStatus.CREATED).body(productResponse.getResponseObjects().get(0));
			} else {
				System.out.println(productResponse.getErrorMessages());
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@GetMapping(value = { "orders", "orders/{page}" })
	public ResponseEntity<List<Order>> getAllOrders(
			@RequestParam(name = "orderStatus", required = false, defaultValue = "All") Optional<String> orderStatus,
			@RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size,
			@PathVariable(name = "page", required = false) Optional<Integer> page) {
		int pageToFetch = page.isPresent() && page.get() > 0 ? page.get() - 1 : 0;
		System.out.println(page);
		OrderSearchConfig config = new OrderSearchConfig();
		if (orderStatus.isPresent()) {
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
			case "DELIVERY_COMPLETE":
				config.setStatus(OrderStatus.DELIVERY_COMPLETED);
				break;
			case "CANCELED":
				config.setStatus(OrderStatus.CANCELED);
				break;
			default:
				config.setStatus(null);
				break;
			}
		}
		ServiceResponse<Order> orderResponse = orderSerivce.getOrders(config, pageToFetch, size.get());
		if (orderResponse.isSucessful()) {
			return ResponseEntity.ok(orderResponse.getResponseObjects());
		} else {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}

	@GetMapping(value = { "orders/order/{orderId}" })
	public ResponseEntity<Order> getSingleOrder(
			@PathVariable(name = "orderId", required = false) Optional<Integer> orderId) {
		if (orderId.isPresent()) {
			ServiceResponse<Order> orderResponse = orderSerivce.getOrderById(orderId.get());

			if (orderResponse.isSucessful()) {
				return ResponseEntity.ok(orderResponse.getResponseObjects().get(0));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping(value = { "orders/order/{orderId}" })
	public ResponseEntity<Order> putSingleOrder(
			@PathVariable(name = "orderId", required = false) Optional<Integer> orderId,
			@RequestParam(name = "orderStatus", required = true) Optional<String> orderStatus) {
		if (orderId.isPresent() && orderStatus.isPresent()) {

			ServiceResponse<Order> orderResponse = null;
			switch (orderStatus.get()) {
			case "NOT_HANDLED":
				orderResponse = orderSerivce.setStatus(orderId.get(), OrderStatus.NOT_HANDLED);
				break;
			case "DISPATCHED":
				orderResponse = orderSerivce.setStatus(orderId.get(), OrderStatus.DISPATCHED);
				break;
			case "DELIVERY":
				orderResponse = orderSerivce.setStatus(orderId.get(), OrderStatus.DELIVERY);
				break;
			case "DELIVERY_COMPLETE":
				orderResponse = orderSerivce.setStatus(orderId.get(), OrderStatus.DELIVERY_COMPLETED);
				break;
			case "CANCELED":
				orderResponse = orderSerivce.setStatus(orderId.get(), OrderStatus.CANCELED);
				break;
			default:
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
			if (orderResponse.isSucessful()) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
			} else {
				System.out.println(orderResponse.getErrorMessages());
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PostMapping(value = { "users/new" },consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Customer> getAllProducuts(@Valid @RequestBody Optional<Customer> newCustomer) {
		System.out.println(newCustomer);
		if(newCustomer.isPresent()) {
			ServiceResponse<Customer> customerResponse = accountService.createCustomer(newCustomer.get());
			if (customerResponse.isSucessful()) {
				return ResponseEntity.ok(customerResponse.getResponseObjects().get(0));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
			
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

	}

}
