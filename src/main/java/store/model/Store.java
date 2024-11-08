package store.model;

import java.util.ArrayList;
import java.util.List;
import store.utils.message.ErrorMessage;

public class Store {

    private final Inventory inventory;

    public Store(Inventory inventory) {
        this.inventory = inventory;
    }

    //TODO 프로모션 상품 재고가 남아있지 않은 경우
    //TODO 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우
    public int calculateTotalAmount(List<Order> orders) {
        int totalAmount = 0;
        for (Order order : orders){
            Product product = getProductAtInventory(order);
            totalAmount += order.getQuantity() * product.getPrice();
        }
        return totalAmount;
    }

    public int calculateDiscountAmount(List<Order> orders) {
        int totalDiscountAmount = 0;
        for (Order order : orders){
            Product product = getProductAtInventory(order);
            Promotion promotion = product.getPromotion();

            if (promotion != null) {
                totalDiscountAmount += promotion.calculateDiscount(order.getQuantity(), product.getPrice(), order.getOrderDate());
            }
        }
        return totalDiscountAmount;
    }
    public int calculateFinalAmount(List<Order> orders) {
        return calculateTotalAmount(orders) - calculateDiscountAmount(orders);
    }

    public List<Order> processOrder(List<Order> orders){
        List<Order> successOrders = new ArrayList<>();
        for (Order order : orders){
            updateStock(order);
            successOrders.add(order);
        }
        return successOrders;
    }


    /* --------------------------------------------------------------------------------------------*/

    private Product getProductAtInventory(Order order) {
        return inventory.findByProductName(order.getProductName());
    }


    private void updateStock(Order order) {
        Product product = getProduct(order);
        Integer quantity = order.getQuantity();
        inventory.reduceStock(product, quantity);
    }

    private Product getProduct(Order order) {
        Product product = getProductAtInventory(order);
        checkExistProduct(product);
        return product;
    }

    private void checkExistProduct(Product product) {
        if (!inventory.existsByProduct(product)){
            throw new IllegalArgumentException(ErrorMessage.NON_EXIST_PRODUCT);
        }
    }

}
