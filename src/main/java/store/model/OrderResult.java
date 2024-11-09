package store.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderResult {

    private static final Integer ZERO = 0;
    private static final Double MEMBERSHIP = 0.30;

    private final Map<Product, Integer> orderedProducts;
    private final LocalDate orderDate;

    public OrderResult(Map<Product, Integer> orderedProducts) {
        this.orderedProducts = orderedProducts;
        this.orderDate = LocalDate.now();
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

    public boolean hasPromotionAppliedForProductName(String productName) {
        for (Product product : orderedProducts.keySet()) {
            if (product.getName().equals(productName) && product.getPromotion() != null) {
                return true;
            }
        }
        return false;
    }


    public int calculateTotalAmount() {
        int totalAmount = ZERO;
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            totalAmount += product.getPrice() * orderedProducts.get(product);
        }
        return totalAmount;
    }

    public int calculateDiscountAmount() {
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
        int totalNonPromotedPrice = calculateTotalAmount() - calculateDiscountAmount();

        return (int) (totalNonPromotedPrice * MEMBERSHIP);
    }

    public int calculateFinalAmount(Integer membershipDiscount) {
        return calculateTotalAmount() - (calculateDiscountAmount() + membershipDiscount);
    }

    public int calculatePromotionBonusQuantity(Product product){
        if (!product.hasPromotion()){
            return 0;
        }
        int quantity = orderedProducts.get(product);
        return product.getPromotion().countPromotionAmount(quantity);
    }

    public int getQuantityByProductName(String productName){
        Set<Product> products = orderedProducts.keySet();
        int total = 0;
        for (Product product : products){
            if (product.getName().equals(productName)){
                total += orderedProducts.get(product);
            }
        }
        return total;
    }


    public int calculatePromotionIsNotApplied(Product product){
        Promotion promotion = product.getPromotion();

        return getQuantityByProductName(product.getName()) - (calculatePromotionBonusQuantity(product)
                * (promotion.getBuyAmount() + promotion.getGetAmount()));
    }


    public LocalDate getOrderDate() {
        return orderDate;
    }

    /* -------------------------------------------------------------------------------------------------------------------*/
}
