package store.controller;


import static store.utils.message.InputMessage.*;

import java.util.List;
import java.util.Map;
import store.converter.OrderConverter;
import store.converter.ProductConverter;
import store.converter.PromotionConverter;
import store.model.Inventory;
import store.model.Order;
import store.model.Product;
import store.model.Promotion;
import store.model.Store;
import store.service.StoreService;
import store.utils.message.InputMessage;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    public Store openStore(){
        OutputView.printWelcomeMessage();

        List<String> inputPromotions = InputView.inputData("src/main/resources/promotions.md");
        List<String> inputProducts = InputView.inputData("src/main/resources/products.md");

        List<Promotion> promotions = PromotionConverter.convertToPromotion(inputPromotions);
        Map<Product, Integer> products = ProductConverter.convertToProduct(inputProducts, promotions);

        Store store = storeService.initializeStore(products);
        OutputView.printProducts(products);

        return store;
    }

    public void getOrder(Store store){

        String inputOrder = InputView.input(ORDER_MESSAGE);
        List<Order> orders = OrderConverter.convertToOrder(inputOrder);
        Map<Product, Integer> productIntegerMap = storeService.processOrder(orders, store);

        checkFreeItem(productIntegerMap, store);
    }

    private void checkFreeItem(Map<Product, Integer> orderResults, Store store){
        List<Product> eligibleFreeItems = storeService.checkEligibleFreeItems(orderResults, store);

        for (Product product : eligibleFreeItems){
            OutputView.printFreePromotionItems(product, 1);
        }
    }
}
