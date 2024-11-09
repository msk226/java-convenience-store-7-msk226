package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Store {


    private static final Integer ZERO = 0;
    private static final Double MEMBERSHIP = 0.30;
    private final Inventory inventory;
    private Map<Product, Integer> orderResult;

    public Store(Inventory inventory) {
        this.inventory = inventory;
        orderResult = new HashMap<>();
    }
    public Map<Product, Integer> processOrder(List<Order> orders){
        inventory.checkOrderIsPossible(orders);
        Map<Product, Integer> orderResult = inventory.retrieveProductForOrder(orders);
        updateOrderResult(orderResult);
        return orderResult;
    }

    public boolean checkEligibleFreeItems(Product product, Integer quantity){
        if (!product.hasPromotion() || !inventory.isEligibleFreeItems(product, quantity)){
            return false;
        }
        return product.getPromotion().checkEligibleFreeItems(quantity);
    }

    public Map<Product, Integer> getFreeItem(Map<Product, Integer> orderResult, Product product, Integer quantity){
        return inventory.giveFreeItem(orderResult, product, quantity);
    }

    public int getPrice(String productName){
       return inventory.getPrice(productName);
    }

    public int calculateTotalAmount(Map<Product, Integer> orderResult) {
        int totalAmount = ZERO;
        Set<Product> products = orderResult.keySet();
        for (Product product : products){
            totalAmount += product.getPrice() * orderResult.get(product);
        }
        return totalAmount;
    }

    public int calculateDiscountAmount(Map<Product, Integer> orderResult, LocalDate orderDate) {
        int totalDiscountAmount = ZERO;
        Set<Product> products = orderResult.keySet();
        for (Product product : products){
            Promotion promotion = product.getPromotion();
            if (promotion != null) {
                totalDiscountAmount += promotion.calculateDiscount(orderResult.get(product), product.getPrice(), orderDate);
            }
        }
        return totalDiscountAmount;
    }

    public int calculateMembershipAmount(Map<Product, Integer> orderResult){
        int totalNonPromotedPrice = 0;

        for (Map.Entry<Product, Integer> entry : orderResult.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (product.getPromotion() == null) {
                totalNonPromotedPrice += product.getPrice() * quantity;
            }
        }

        return (int) (totalNonPromotedPrice * MEMBERSHIP);
    }

    public int calculateFinalAmount(Map<Product, Integer> orderResult, LocalDate orderDate) {
        return calculateTotalAmount(orderResult) - calculateDiscountAmount(orderResult, orderDate);
    }

    public Map<Product, Integer> getOrderResult() {
        return orderResult;
    }

    private void updateOrderResult(Map<Product, Integer> orderResult){
        this.orderResult = orderResult;
    }
    /* --------------------------------------------------------------------------------------------*/


}
