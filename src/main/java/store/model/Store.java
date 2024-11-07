package store.model;

import store.utils.ErrorMessage;

public class Store {

    private final Inventory inventory;

    public Store(Inventory inventory) {
        this.inventory = inventory;
    }

    public void processOrder(Order order){
        Product product = order.getProduct();
        Integer quantity = order.getQuantity();

        checkExistProduct(product);

        inventory.reduceStock(product, quantity);
    }

    private void checkExistProduct(Product product) {
        if (!inventory.existsByProduct(product)){
            throw new IllegalArgumentException(ErrorMessage.NON_EXIST_PRODUCT);
        }
    }

}
