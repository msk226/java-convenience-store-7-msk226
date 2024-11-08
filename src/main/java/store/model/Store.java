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

    //TODO 프로모션 상품 재고가 남아있지 않은 경우
    //TODO 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우
    public int calculateTotalAmount(Map<Product, Integer> orderResult) {
        int totalAmount = 0;
        Set<Product> products = orderResult.keySet();
        for (Product product : products){
            totalAmount += product.getPrice() * orderResult.get(product);
        }
        return totalAmount;
    }

    public int calculateDiscountAmount(Map<Product, Integer> orderResult, LocalDate orderDate, boolean isGetFreeItem) {
        int totalDiscountAmount = 0;
        Set<Product> products = orderResult.keySet();
        for (Product product : products){
            Promotion promotion = product.getPromotion();
            if (promotion != null) {
                if ((promotion.checkEligibleFreeItems(orderResult.get(product)) != 0) && isGetFreeItem){
                    //TODO 주문에 무료 상품 하나 추가
                }

                totalDiscountAmount += promotion.calculateDiscount(orderResult.get(product), product.getPrice(), orderDate);
            }
        }
        return totalDiscountAmount;
    }

    public int calculateFinalAmount(Map<Product, Integer> orderResult, LocalDate orderDate) {
        return calculateTotalAmount(orderResult) - calculateDiscountAmount(orderResult, orderDate);
    }

    public Map<Product, Integer> processOrder(List<Order> orders){
        for (Order order : orders){
            inventory.checkOrderIsPossible(order);

            Map<Product, Integer> productForOrder = inventory.retrieveProductForOrder(orders);
        }
        return successOrders;
    }


    /* --------------------------------------------------------------------------------------------*/


}
