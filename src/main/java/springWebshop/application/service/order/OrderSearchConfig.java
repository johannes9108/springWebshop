package springWebshop.application.service.order;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.model.domain.order.Order.OrderStatus;

@Getter
@Setter
public class OrderSearchConfig {

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
