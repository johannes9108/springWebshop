package springWebshop.application.model.domain.order;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderTest {

    Order order;
    OrderLine orderLine1;
    OrderLine orderLine2;

    @BeforeAll
    void setUp() {
        order = new Order();
        orderLine1 = new OrderLine();
        orderLine1.setProductId(1);
        orderLine1.setSum(80);
        orderLine1.setVatPercentage(0.25);
        orderLine1.setVatSum(20.00);
        orderLine1.setSumPayable(100);
        orderLine1.setItemQuantity(1);

        orderLine2 = new OrderLine();
        orderLine2.setProductId(2);
        orderLine2.setSum(125);
        orderLine2.setVatPercentage(0.12);
        orderLine2.setVatSum(15.00);
        orderLine2.setSumPayable(140);
        orderLine2.setItemQuantity(5);
        order.addOrderLine(orderLine1);
        order.addOrderLine(orderLine2);
    }

    @Test
    void getTotalNumberOfItem() {
        assertEquals(6, order.getTotalNumberOfItem());
    }

    @Test
    void getTotalSum() {
        assertEquals(205, order.getTotalSum());
    }

    @Test
    void getTotalVatSum() {
        assertEquals(35, order.getTotalVatSum());
    }

    @Test
    void getTotalPayable() {
        assertEquals(240, order.getTotalPayable());
    }
    @Test
    void getVatPercentages() {
        assertEquals(20, order.getVatPercentages().get(0.25));
        assertEquals(15, order.getVatPercentages().get(0.12));
    }

    @Test
    void removeOrderLine(){
        order.removeOrderLine(orderLine1);
        assertEquals(5, order.getTotalNumberOfItem());
        assertEquals(125, order.getTotalSum());
        assertEquals(15, order.getTotalVatSum());
        assertEquals(140, order.getTotalPayable());
        assertEquals(null, order.getVatPercentages().get(0.25));
        assertEquals(15, order.getVatPercentages().get(0.12));
    }
}