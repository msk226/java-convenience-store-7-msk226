package store.model;

import java.util.ArrayList;
import java.util.List;
import store.utils.message.ErrorMessage;

public class Store {

    private final Inventory inventory;

    public Store(Inventory inventory) {
        this.inventory = inventory;
    }

    public int calculateTotalAmount(List<Order> orders) {
        int totalAmount = 0;
        for (Order order : orders){
            totalAmount += order.getTotalAmount();
        }
        return totalAmount;
    }

    public int calculateDiscountAmount(List<Order> orders) {
        int totalDiscountAmount = 0;
        for (Order order : orders){
            totalDiscountAmount += order.getDiscountAmount();
        }
        return totalDiscountAmount;
    }

    public int calculateFinalAmount(List<Order> orders) {
        return calculateTotalAmount(orders) - calculateDiscountAmount(orders);
    }

    public List<Order> processOrder(List<Order> orders){
        List<Order> successOrders = new ArrayList<>();
        for (Order order : orders){
            Product product = order.getProduct();
            Integer quantity = order.getQuantity();
            checkExistProduct(product);
            inventory.reduceStock(product, quantity);
            successOrders.add(order);
        }
        return successOrders;
    }

    private void checkExistProduct(Product product) {
        if (!inventory.existsByProduct(product)){
            throw new IllegalArgumentException(ErrorMessage.NON_EXIST_PRODUCT);
        }
    }

}
