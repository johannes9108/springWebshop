package springWebshop.application.model.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import springWebshop.application.model.domain.Currency;
import springWebshop.application.model.domain.Product;
import springWebshop.application.service.ServiceResponse;
import springWebshop.application.service.product.ProductService;

public class ShoppingCartDTO {
    private Currency currency;
    @Getter
    private Map<Product, Integer> productMap;
    private ProductService productService;

    public ShoppingCartDTO(ProductService productService) {
        this.productService = productService;
        this.currency = Currency.SEK;
        this.productMap = new LinkedHashMap<>();
    }

    public double getTotalSum() {
        final double[] totalSum = {0.0};
        productMap.forEach((item, quantity) -> {
            totalSum[0] += item.getBasePrice() * quantity;
        });
        return totalSum[0];
    }
    public int getTotalItems() {
    	return productMap.values().stream().reduce(0,(subTotal,quantity)->subTotal+quantity);
    }

    public double getItemSum(Product item) {
        return productMap.get(item) * item.getBasePrice();
    }

    public int getCartQuantityByProductId(int productId) {
        Optional<Product> product = productMap
                .entrySet()
                .stream()
                .filter(cartProduct -> cartProduct.getKey().getId() == productId)
                .map(cartProduct2 -> cartProduct2.getKey())
                .findFirst();

        return product.isPresent()
                ? productMap.getOrDefault(product.get(), 0)
                : 0;
    }

    public boolean addItem(long productId) {
        ServiceResponse<Product> foundProduct = productService.getProductById(productId);
        if (foundProduct.isSucessful()) {
            try {
                Product product = foundProduct.getResponseObjects().get(0);
                int currentQuantity = productMap.getOrDefault(product, 0);
                productMap.put(product, ++currentQuantity);
                return true;
            } catch (Exception exception) {
                return false;
            }
        } else return false;
    }

    public boolean removeItem(long productId) {
        Optional<Product> product = productMap
                .entrySet()
                .stream()
                .filter(cartProduct -> cartProduct.getKey().getId() == productId)
                .map(cartProduct2 -> cartProduct2.getKey())
                .findFirst();
        if (product.isPresent()) {
            return removeOrDeductCartItem(product.get());
        } else return false;

    }

    private boolean removeOrDeductCartItem(Product product) {
        try {
            int currentQuantity = productMap.getOrDefault(product, 0);
            if (currentQuantity > 1) {
                productMap.put(product, --currentQuantity);
            } else {
                productMap.remove(product);
            }
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

	public void dispose() {
		productMap.clear();
	}

//    @Override
//    public String toString() {
////		System.out.println(productMap);
//        return productMap
//                .entrySet()
//                .stream()
//                .map(product -> product.getKey().getName() + ":" + product.getValue())
//                .collect(Collectors.toSet()).toString();
//    }

}
