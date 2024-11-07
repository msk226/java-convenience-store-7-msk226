package store.model;

import java.util.ArrayList;
import java.util.List;
import store.utils.ErrorMessage;

public class Store {

    private final Inventory inventory;

    public Store(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Payment> processOrder(List<Order> orders){
        List<Payment> payments = new ArrayList<>();
        for (Order order : orders){
            Product product = order.getProduct();
            Integer quantity = order.getQuantity();

            checkExistProduct(product);

            inventory.reduceStock(product, quantity);
            Payment payment = new Payment(order);
            payments.add(payment);
        }
        return payments;
    }

    private void checkExistProduct(Product product) {
        if (!inventory.existsByProduct(product)){
            throw new IllegalArgumentException(ErrorMessage.NON_EXIST_PRODUCT);
        }
    }

}
