package springWebshop.application.integration.order;

import org.springframework.data.domain.Page;

import springWebshop.application.model.domain.order.Order;
import springWebshop.application.service.order.OrderSearchConfig;

public interface OrderRepositoryCustom {
    Page<Order> getOrders(OrderSearchConfig config, int page, int size);
}
