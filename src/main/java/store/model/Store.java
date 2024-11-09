package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Store {

    private final Inventory inventory;

    public Store(Inventory inventory) {
        this.inventory = inventory;
    }

    /* -------------------------------------------------------------------------------------------------------------------*/

    public Map<Product, Integer> getProducts() {
        return inventory.getStock();
    }

    public OrderResult processOrder(List<Order> orders) {
        inventory.checkOrderIsPossible(orders);
        return new OrderResult(inventory.retrieveProductForOrder(orders));
    }

    public boolean checkEligibleFreeItems(Product product, Integer quantity) {
        if (!product.hasPromotion() || !inventory.isEligibleFreeItems(product, quantity)) {
            return false;
        }
        return product.getPromotion().checkEligibleFreeItems(quantity);
    }

    public OrderResult getFreeItem(OrderResult orderResult, Product product, Integer quantity) {
        return inventory.giveFreeItem(orderResult, product, quantity);
    }

    public int getPrice(String productName) {
        return inventory.getPrice(productName);
    }

    /* --------------------------------------------------------------------------------------------*/


}
