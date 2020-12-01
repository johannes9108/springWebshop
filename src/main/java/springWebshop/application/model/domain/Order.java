package springWebshop.application.model.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.model.domain.user.Address;
import springWebshop.application.model.domain.user.Customer;

@Getter
@Setter
@Entity(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<OrderLine> orderLines;
    private int totalNumberOfItem;
    private double totalSum;
    private double totalVatSum;
    private double totalDiscount;
    private double totalPayable;
    private Currency currency;
    private Date created;
    private Date dispatched;
    private Date InDelivery;
    private Date deliveryComplete;
    private Address deliveryAddress;
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

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderLines=" + orderLines + ", totalNumberOfItem=" + totalNumberOfItem
                + ", totalSum=" + totalSum + ", totalVatSum=" + totalVatSum + ", vatPercentages=" + ""
                + ", totalDiscount=" + totalDiscount + ", totalPayable=" + totalPayable + ", currency=" + currency
                + "]";
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

    private void clearTotalSums(){
        this.totalDiscount = this.totalPayable = this.totalSum = this.totalVatSum = this.totalNumberOfItem = 0;
    }

}
