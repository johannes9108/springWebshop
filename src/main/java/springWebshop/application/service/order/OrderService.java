package springWebshop.application.service.order;

import springWebshop.application.model.domain.Address;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.viewModels.ShoppingCart;
import springWebshop.application.service.ServiceResponse;

public interface OrderService {

    ServiceResponse<Order> getOrderById(long id);

    ServiceResponse<Order> getAllOrders();
    
    ServiceResponse<Order> getAllOrdersByCustomerId(long id);

    ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig);
    ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig, int page);
    ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig, int page, int size);

    ServiceResponse<Order> create(Order newOrder);

    ServiceResponse<Order> createOrderFromShoppingCart(ShoppingCart shoppingCart,
                                                       long customerId, Address deliveryAddress);

    ServiceResponse<Order> setStatus(long orderId, Order.OrderStatus orderStatus);

    //ToDo Decide if use:
    //ServiceResponse<Order> update(Order updatedOrder);

}
