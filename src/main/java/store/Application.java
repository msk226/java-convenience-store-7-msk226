package store;

import store.controller.StoreController;
import store.service.StoreService;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(new StoreService());
        storeController.openStore();
    }
}
