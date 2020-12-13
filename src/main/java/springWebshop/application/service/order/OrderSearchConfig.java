package springWebshop.application.service.order;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.model.domain.order.Order.OrderStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchConfig {

    Order.OrderStatus status;
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
