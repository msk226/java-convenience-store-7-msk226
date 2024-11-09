package store.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderResult {

    private static final Integer ZERO = 0;
    private static final Double MEMBERSHIP = 0.30;

    private final Map<Product, Integer> orderedProducts;

    public OrderResult(Map<Product, Integer> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    /* -------------------------------------------------------------------------------------------------------------------*/

    public Map<Product, Integer> updateOrderedProducts(Product product, Integer quantity){
        Integer existQuantity = orderedProducts.get(product);
        orderedProducts.put(product, existQuantity + quantity);
        return orderedProducts;
    }
    public Map<Product, Integer> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getQuantity(Product product){
        return orderedProducts.getOrDefault(product, 0);
    }

    public int calculateTotalAmount() {
        int totalAmount = ZERO;
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            totalAmount += product.getPrice() * orderedProducts.get(product);
        }
        return totalAmount;
    }

    public int calculateDiscountAmount(LocalDate orderDate) {
        int totalDiscountAmount = ZERO;
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            Promotion promotion = product.getPromotion();
            if (promotion != null) {
                totalDiscountAmount += promotion.calculateDiscount(orderedProducts.get(product), product.getPrice(), orderDate);
            }
        }
        return totalDiscountAmount;
    }

    public int calculateMembershipAmount(){
        int totalNonPromotedPrice = 0;

        for (Map.Entry<Product, Integer> entry : orderedProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (product.getPromotion() == null) {
                totalNonPromotedPrice += product.getPrice() * quantity;
            }
        }

        return (int) (totalNonPromotedPrice * MEMBERSHIP);
    }

    public int calculateFinalAmount(LocalDate orderDate) {
        return calculateTotalAmount() - calculateDiscountAmount(orderDate);
    }

    /* -------------------------------------------------------------------------------------------------------------------*/
}
