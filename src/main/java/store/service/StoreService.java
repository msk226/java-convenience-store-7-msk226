package store.service;

import java.util.Map;
import store.model.Inventory;
import store.model.Product;
import store.model.Store;

public class StoreService {

    public Store initializeStore(Map<Product, Integer> products){
        Inventory inventory = new Inventory();
        inventory.addProducts(products);
        return new Store(inventory);
    }



}
