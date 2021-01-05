package springWebshop.application.service.order;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Address;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.dto.ShoppingCartDTO;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductSearchConfig;

public interface OrderService {

    ServiceResponse<Order> getOrderById(long id);

    ServiceResponse<Order> getAllOrders();
    
    ServiceResponse<Order> getAllOrdersByCustomerId(long id);

//    ServiceResponse<Order> getAllOrders();
//
//    ServiceResponse<Order> getAllOrders(int page);
//
//    ServiceResponse<Order> getAllOrders(int page, int size);

    ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig);
    ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig, int page);
    ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig, int page, int size);

    ServiceResponse<Order> create(Order newOrder);

    ServiceResponse<Order> createOrderFromShoppingCart(ShoppingCartDTO shoppingCartDTO,
                                                       long customerId, Address deliveryAddress);

    ServiceResponse<Order> setStatus(long orderId, Order.OrderStatus orderStatus);

    //ToDo Decide if use:
    //ServiceResponse<Order> update(Order updatedOrder);

}
