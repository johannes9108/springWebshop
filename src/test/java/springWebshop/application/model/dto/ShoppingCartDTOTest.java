package springWebshop.application.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springWebshop.application.mockservices.ProductServiceMock;
import springWebshop.application.model.domain.Product;
import springWebshop.application.service.product.ProductService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartDTOTest {

    ShoppingCartDTO cart;
    ProductServiceMock productServiceMock;

    @BeforeEach
    void init() {
        productServiceMock = new ProductServiceMock();
        cart = new ShoppingCartDTO(productServiceMock);
    }

    private List<Product> createAndGetXProductsWithPriceY (int numberOfProducts, double price) {
        ArrayList<Product> productList = new ArrayList<>();
        for (int i = 1; i <= numberOfProducts; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setId(i);
            product.setBasePrice(price);
            productList.add(product);
        }
        productServiceMock.loadFakeDb(productList);
        return productList;
    }

    private void printCart(){
        cart.getProductMap().forEach((product, integer) -> System.out.println("ProductId: " + product.getId() + ", Quantity: " + integer));
    }

    @Test
    void getTotalSum() {
        List<Product> productList = createAndGetXProductsWithPriceY(2,10.00);
        productList.forEach(product -> cart.addItem(product.getId()));
        assertEquals(20, cart.getTotalSum());
    }

    @Test
    void getTotalItems1() {
        createAndGetXProductsWithPriceY(3,10.00);
        cart.addItem(1);
        cart.addItem(2);
        cart.addItem(3);
        assertEquals(3, cart.getTotalItems());
    }

    @Test
    void getTotalItems2() {
        createAndGetXProductsWithPriceY(1,10.00);
        cart.addItem(1);
        cart.addItem(1);
        cart.addItem(1);
        assertEquals(3, cart.getTotalItems());
    }

    @Test
    void getItemSum() {
        List<Product> productList = createAndGetXProductsWithPriceY(9, 9);
        productList.forEach(product -> cart.addItem(product.getId()));
        assertEquals(81, cart.getTotalSum());
    }

    @Test
    void getCartQuantityByProductId() {
        createAndGetXProductsWithPriceY(1,10);
        for (int i = 0; i < 7; i++) {
            cart.addItem(1);
        }
        assertEquals(7, cart.getCartQuantityByProductId(1));
    }

    @Test
    void addItem() {
        assertEquals(0, cart.getTotalItems());
        createAndGetXProductsWithPriceY(1, 10);
        cart.addItem(1);
        assertEquals(1, cart.getTotalItems());
    }

    @Test
    void removeItem1() {
        createAndGetXProductsWithPriceY(2, 10);
        cart.addItem(1);
        cart.addItem(2);
        assertEquals(2, cart.getTotalItems());
        cart.removeItem(1);
        assertEquals(1, cart.getTotalItems());
    }

    @Test
    void removeItem2() {
        createAndGetXProductsWithPriceY(2, 10);
        cart.addItem(1);
        cart.addItem(1);
        cart.addItem(2);
        assertEquals(3, cart.getTotalItems());
        cart.removeItem(1);
        assertEquals(2, cart.getTotalItems());
    }

    @Test
    void dispose() {
        createAndGetXProductsWithPriceY(2, 10);
        cart.addItem(1);
        cart.addItem(1);
        cart.addItem(2);
        assertEquals(3, cart.getTotalItems());
        cart.dispose();
        assertEquals(0, cart.getTotalItems());
    }
}