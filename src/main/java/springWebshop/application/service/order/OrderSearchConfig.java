package springWebshop.application.service.order;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springWebshop.application.model.domain.order.Order;
import springWebshop.application.model.domain.order.Order.OrderStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchConfig {

    OrderStatus	 status;
    LocalDate createdEarliest;
    LocalDate createdLatest;
    LocalDate dispatchedEarliest;
    LocalDate dispatchedLatest;
    LocalDate sentForDeliveryEarliest;
    LocalDate sentForDeliveryLatest;
    Double minTotalSum;
    Double maxTotalSum;
    Long customerId;
    SortBy sortBy;
    
    public static enum SortBy {
        created,
        dispatched,
        inDelivery,
        deliveryComplete,
        canceled,
        totalSum
    }

    

}
