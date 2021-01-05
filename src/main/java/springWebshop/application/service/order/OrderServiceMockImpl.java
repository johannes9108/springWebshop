package springWebshop.application.service.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import springWebshop.application.model.domain.order.Address;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.order.OrderLine;
import springWebshop.application.model.domain.order.Order.OrderStatus;
import springWebshop.application.model.domain.user.CustomerAddress;
import springWebshop.application.model.dto.ShoppingCartDTO;
import springWebshop.application.service.ServiceResponse;

@Service("Mock")
public class OrderServiceMockImpl implements OrderService {

	List<Order> orderStore;
	static int orderIdGenerator = 0;
	static int orderLineIdGenerator = 0;

	public OrderServiceMockImpl() {
		orderStore = new ArrayList<Order>();
		for (int i = 0; i < 5; i++) {
			List<OrderLine> orderlines = new ArrayList<OrderLine>();
			for (int j = 0; j < 10; j++) {
				OrderLine orderLine = new OrderLine();
				orderLine.setId(++orderLineIdGenerator);
				orderLine.setDiscount(new Random().nextDouble()*100);
				orderLine.setSum(new Random().nextDouble()*100);
				orderlines.add(orderLine);
			}
			Order localOrder = new Order();
			localOrder.setId((long) ++orderIdGenerator);
			localOrder.setTotalSum(new Random().nextDouble()*100);
			localOrder.setTotalVatSum(new Random().nextDouble()*100);
//			localOrder.setOrderLines(orderlines);
			orderStore.add(localOrder);
		}
	}

	@Override
	public ServiceResponse<Order> getOrderById(long id) {
		ArrayList<Order> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		try {
			responseObjects.add(orderStore.get(new Long(id - 1).intValue()));

		} catch (IndexOutOfBoundsException e) {
			errorMessages.add("Couldnt find order with id: " + id);
		}
		ServiceResponse<Order> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Order> getAllOrders() {
		System.out.println("Mock GetAllOrders");
		ArrayList<Order> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
		responseObjects.addAll(orderStore);
		ServiceResponse<Order> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig) {
		return null;
	}

	@Override
	public ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig, int page) {
		return null;
	}

	@Override
	public ServiceResponse<Order> getOrders(OrderSearchConfig orderSearchConfig, int page, int size) {
		return null;
	}

//	@Override
//	public ServiceResponse<Order> getAllOrders(int page, int size) {
//		ArrayList<Order> responseObjects = new ArrayList<>();
//		ArrayList<String> errorMessages = new ArrayList<>();
//		responseObjects.addAll(paginatedList(page, size));
//		ServiceResponse<Order> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);
//
//		return serviceResponse;
//	}

	private List<Order> paginatedList(int page, int size) {
		ArrayList<Order> list = new ArrayList<>();

		for (int i = page * size; i < page * size + size; i++) {
			list.add(orderStore.get(i));
		}

		return list;
	}

	@Override
	public ServiceResponse<Order> create(Order newOrder) {
		ArrayList<Order> responseObjects = new ArrayList<>();
		ArrayList<String> errorMessages = new ArrayList<>();
//		newOrder.setId(++idGenerator);
		orderStore.add(newOrder);
		ServiceResponse<Order> serviceResponse = new ServiceResponse<>(responseObjects, errorMessages);

		return serviceResponse;
	}

	@Override
	public ServiceResponse<Order> createOrderFromShoppingCart(ShoppingCartDTO shoppingCartDTO, long customerId, Address deliveryAddress) {
		return null;
	}

	@Override
	public ServiceResponse<Order> setStatus(long orderId, OrderStatus orderStatus) {
		return null;
	}

	@Override
	public ServiceResponse<Order> getAllOrdersByCustomerId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public ServiceResponse<Order> getAllOrders(int page) {
//		// TODO Auto-generated method stub
//		return null;
//	}


}
