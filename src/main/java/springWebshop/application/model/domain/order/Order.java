
package springWebshop.application.model.domain.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springWebshop.application.model.domain.user.Customer;

@Getter
@Setter
@Entity(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;
    private int totalNumberOfItem;
    private double totalSum;
    private double totalVatSum;
    private double totalDiscount;
    private double totalPayable;
    private Currency currency;
	private LocalDate created;
    private LocalDate dispatched;
    private LocalDate inDelivery;
    private LocalDate deliveryComplete;
    private LocalDate canceled;
    @Embedded
    private DeliveryAddress deliveryAddress;
    @ManyToOne
    private Customer customer;

    public Order() {
        orderLines = new ArrayList<OrderLine>();
    }

    public Order(int totalNumberOfItem, double totalSum, double totalVatSum, double totalDiscount,
                 double totalPayable) {
        orderLines = new ArrayList<OrderLine>();
        this.totalNumberOfItem = totalNumberOfItem;
        this.totalSum = totalSum;
        this.totalVatSum = totalVatSum;
        this.totalDiscount = totalDiscount;
        this.totalPayable = totalPayable;
    }

    public String getHeaderString() {
        return "OrderId =" + id + " OrderStatus =" + getOrderStatus() + "\nTotalNumberOfItem=" + totalNumberOfItem
                + ", totalSum=" + totalSum + ", totalVatSum=" + totalVatSum + ", vatPercentages=" + getVatPercentages()
                + ", totalDiscount=" + totalDiscount + ", totalPayable=" + totalPayable + ", currency=" + currency
                + "]";
    }

    @Override
    public String toString() {
        return "OrderId =" + id + " OrderStatus =" + getOrderStatus() + orderLines + "\nTotalNumberOfItem=" + totalNumberOfItem
                + ", totalSum=" + totalSum + ", totalVatSum=" + totalVatSum + ", vatPercentages=" + getVatPercentages()
                + ", totalDiscount=" + totalDiscount + ", totalPayable=" + totalPayable + ", currency=" + currency
                + "]";
    }

    public OrderStatus getOrderStatus() {
        if (this.canceled != null) return OrderStatus.CANCELED;
        if (this.deliveryComplete != null) return OrderStatus.DELIVERY_COMPLETED;
        if (this.inDelivery != null) return OrderStatus.DELIVERY;
        if (this.dispatched != null) return OrderStatus.DISPATCHED;
        return OrderStatus.NOT_HANDLED;
    }

    public boolean isCancelable(){
        return this.getOrderStatus() == OrderStatus.NOT_HANDLED;
    }

    public HashMap<Double, Double> getVatPercentages() {
        HashMap<Double, Double> vatPercentagesMap = new HashMap<>();
        this.orderLines.forEach(orderLine -> {
            vatPercentagesMap.put(orderLine.getVatPercentage(),
                    vatPercentagesMap.getOrDefault(orderLine.getVatPercentage(), 0.0) + orderLine.getVatSum());
        });
        return vatPercentagesMap;
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
        setTotalSumsFromLines();
    }

    public void removeOrderLine(OrderLine orderLine) {
        this.orderLines = this.orderLines
                .stream()
                .filter(orderLineToRemove -> orderLineToRemove.getProductId() != orderLine.getProductId())
                .collect(Collectors.toList());
        setTotalSumsFromLines();
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
        setTotalSumsFromLines();
    }

    public void clearOrderLines() {
        this.orderLines.clear();
        clearTotalSums();
    }

    private void setTotalSumsFromLines() {
        this.totalNumberOfItem = this.orderLines
                .stream()
                .mapToInt(OrderLine::getItemQuantity)
                .sum();

        this.totalDiscount = this.orderLines
                .stream()
                .mapToDouble(OrderLine::getDiscount)
                .sum();

        this.totalVatSum = this.orderLines
                .stream()
                .mapToDouble(OrderLine::getVatSum)
                .sum();

        this.totalSum = this.orderLines
                .stream()
                .mapToDouble(OrderLine::getSum)
                .sum();

        this.totalPayable = this.orderLines
                .stream()
                .mapToDouble(OrderLine::getSumPayable)
                .sum();
    }

    private void clearTotalSums() {
        this.totalDiscount = this.totalPayable = this.totalSum = this.totalVatSum = this.totalNumberOfItem = 0;
    }

    public Address getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = new DeliveryAddress(address);
    }

    public static enum OrderStatus {
        NOT_HANDLED,
        DISPATCHED,
        DELIVERY,
        DELIVERY_COMPLETED,
        CANCELED
    }

    @Getter
    @NoArgsConstructor
    @Embeddable
    private static class DeliveryAddress implements Address {
        @NotBlank
        private String deliveryStreet;
        @NotBlank
        private int deliveryZipCode;
        @NotBlank
        private String deliveryCity;
        @NotBlank
        private String deliveryCountry;

        private DeliveryAddress(String street, int zipCode, String city, String country) {
            this.deliveryStreet = street;
            this.deliveryZipCode = zipCode;
            this.deliveryCity = city;
            this.deliveryCountry = country;
        }

        private DeliveryAddress(Address address) {
            this.deliveryStreet = address.getStreet();
            this.deliveryZipCode = address.getZipCode();
            this.deliveryCity = address.getCity();
            this.deliveryCountry = address.getCountry();
        }

        public String getStreet() {
            return deliveryStreet;
        }

        public void setStreet(String deliveryStreet) {
            this.deliveryStreet = deliveryStreet;
        }

        public int getZipCode() {
            return deliveryZipCode;
        }

        public void setZipCode(int deliveryZipCode) {
            this.deliveryZipCode = deliveryZipCode;
        }

        public String getCity() {
            return deliveryCity;
        }

        public void setCity(String deliveryCity) {
            this.deliveryCity = deliveryCity;
        }

        public String getCountry() {
            return deliveryCountry;
        }

        public void setCountry(String deliveryCountry) {
            this.deliveryCountry = deliveryCountry;
        }

    }

}
