package store.controller;


import static store.utils.constant.ProductConstant.PRODUCT_FILE_PATH;
import static store.utils.constant.PromotionConstant.PROMOTION_FILE_PATH;
import static store.utils.message.InputMessage.*;
import static store.utils.message.OutputMessage.PROMOTION_DIVISION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.converter.OrderConverter;
import store.converter.ProductConverter;
import store.converter.PromotionConverter;
import store.model.Inventory;
import store.model.Order;
import store.model.OrderResult;
import store.model.Product;
import store.model.Promotion;
import store.model.Store;
import store.service.StoreService;
import store.utils.constant.ProductConstant;
import store.utils.constant.PromotionConstant;
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

        List<String> inputPromotions = InputView.inputData(PROMOTION_FILE_PATH);
        List<String> inputProducts = InputView.inputData(PRODUCT_FILE_PATH);

        List<Promotion> promotions = PromotionConverter.convertToPromotion(inputPromotions);
        Map<Product, Integer> products = ProductConverter.convertToProduct(inputProducts, promotions);

        Store store = storeService.initializeStore(products);
        OutputView.printProducts(products);
        return store;
    }

    public void processOrder(Store store){

        String inputOrder = InputView.input(ORDER_MESSAGE);
        List<Order> orders = OrderConverter.convertToOrder(inputOrder);
        OrderResult orderResult = storeService.processOrder(orders, store);

        getFreeItem(store, orderResult);
        checkPromotionIsNotApplied(orderResult);

        OutputView.printTotalQuantity(orderResult);

        getPromotionAmount(orderResult);

        OutputView.printAmount(storeService.getTotalAmount(orderResult),
                storeService.getDiscountAmount(orderResult),
                storeService.getMembershipAmount(orderResult),
                storeService.getPayAmount(orderResult));
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    private void getFreeItem(Store store, OrderResult orderResult){
        List<Product> eligibleFreeItems = storeService.checkEligibleFreeItems(store, orderResult);
        for (Product product : eligibleFreeItems){
            String message = String.format(PROMOTION_MESSAGE_TEMPLATE, product.getName(), 1);
            String input = InputView.input(message);
            if (!input.equals(YES)){
                continue;
            }
            storeService.getFreeItem(product, store, orderResult);
        }
    }

    private void checkPromotionIsNotApplied(OrderResult orderResult){
        Set<Product> products = orderResult.getOrderedProducts().keySet();
        for (Product product : products){
            if (!product.hasPromotion()){
                String input = InputView.input(
                        String.format(PROMOTION_IS_NOT_APPLY, product.getName(), orderResult.getQuantity(product)));
                if (!input.equals(YES)){
                    return;
                }
            }
        }
    }

    private void getPromotionAmount(OrderResult orderResult){
        Set<Product> products = orderResult.getOrderedProducts().keySet();
        System.out.println(PROMOTION_DIVISION);
        for (Product product : products){
            if (product.hasPromotion()){
                int countPromotionDiscount = storeService.countPromotionDiscount(product, orderResult.getQuantity(product));
                OutputView.printPromotionQuantity(product.getName(), countPromotionDiscount);
            }
        }
    }

    private void getMembershipDiscount(Store store){


    }

}
