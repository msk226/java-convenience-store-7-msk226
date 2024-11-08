package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Store {

    private final Inventory inventory;

    public Store(Inventory inventory) {
        this.inventory = inventory;
    }


    public Map<Product, Integer> processOrder(List<Order> orders){
        inventory.checkOrderIsPossible(orders);
        return inventory.retrieveProductForOrder(orders);
    }

    public boolean checkEligibleFreeItems(Product product, Integer quantity){
        return product.getPromotion().checkEligibleFreeItems(quantity);
    }

    //TODO 프로모션 상품 재고가 남아있지 않은 경우 -> 해결
    //TODO 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우
    public int calculateTotalAmount(Map<Product, Integer> orderResult) {
        int totalAmount = 0;
        Set<Product> products = orderResult.keySet();
        for (Product product : products){
            totalAmount += product.getPrice() * orderResult.get(product);
        }
        return totalAmount;
    }

    public int calculateDiscountAmount(Map<Product, Integer> orderResult, LocalDate orderDate) {
        int totalDiscountAmount = 0;
        Set<Product> products = orderResult.keySet();
        for (Product product : products){
            Promotion promotion = product.getPromotion();
            if (promotion != null) {
                totalDiscountAmount += promotion.calculateDiscount(orderResult.get(product), product.getPrice(), orderDate);
            }
        }
        return totalDiscountAmount;
    }

    public int calculateFinalAmount(Map<Product, Integer> orderResult, LocalDate orderDate) {
        return calculateTotalAmount(orderResult) - calculateDiscountAmount(orderResult, orderDate);
    }


    /* --------------------------------------------------------------------------------------------*/


}
