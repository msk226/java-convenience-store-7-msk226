package store.service;

import java.util.List;
import java.util.Map;
import store.model.Inventory;
import store.model.Order;
import store.model.Product;
import store.model.Store;

public class StoreService {

    public Store initializeStore(Map<Product, Integer> products){
        Inventory inventory = new Inventory();
        inventory.addProducts(products);
        return new Store(inventory);
    }

    public Map<Product, Integer> processOrder(List<Order> orders, Store store) {
        return store.processOrder(orders);
    }

}
