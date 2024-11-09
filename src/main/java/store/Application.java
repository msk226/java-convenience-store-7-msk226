package store;

import store.controller.StoreController;
import store.model.Store;
import store.service.StoreService;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(new StoreService());
        Store store = storeController.openStore();
        storeController.processOrder(store);
    }
}
