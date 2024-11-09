package store.controller;


import static store.utils.message.InputMessage.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        storeService.processOrder(orders, store);

        Map<Product, Integer> orderResult = getFreeItem(store);
        checkPromotionIsNotApplied(store.getOrderResult());

        OutputView.printProducts(orderResult);
    }

    private Map<Product, Integer> getFreeItem (Store store){
        List<Product> eligibleFreeItems = storeService.checkEligibleFreeItems(store);
        Map<Product, Integer> orderResults = new HashMap<>();

        for (Product product : eligibleFreeItems){
            String message = String.format(PROMOTION_MESSAGE_TEMPLATE, product.getName(), 1);
            String input = InputView.input(message);
            if (!input.equals("Y")){
                continue;
            }
            orderResults = storeService.getFreeItem(product, store);
        }
        return orderResults;
    }

    private void checkPromotionIsNotApplied(Map<Product, Integer> orderResult){
        Set<Product> products = orderResult.keySet();
        for (Product product : products){
            if (!product.hasPromotion()){
                String input = InputView.input(
                        String.format(PROMOTION_IS_NOT_APPLY, product.getName(), orderResult.get(product)));
                if (!input.equals("Y")){
                    return;
                }
            }
        }
    }
    
}
