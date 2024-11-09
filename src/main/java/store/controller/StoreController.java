package store.controller;

import static store.utils.constant.ProductConstant.PRODUCT_FILE_PATH;
import static store.utils.constant.PromotionConstant.PROMOTION_FILE_PATH;
import static store.utils.message.ErrorMessage.*;
import static store.utils.message.InputMessage.*;
import static store.utils.message.OutputMessage.WELCOME_MESSAGE;

import java.util.List;
import java.util.Map;
import java.util.Set;

import store.converter.OrderConverter;
import store.converter.ProductConverter;
import store.converter.PromotionConverter;
import store.model.*;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private static final Integer ZERO = 0;
    private static final Integer FREE_ITEM = 1;

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }
    public void openStoreAndProcessOrder() {
        Store store = openStore();
        while (true) {
            try {
                processOrder(store);
                if (!retry()) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                if (!retry()) {
                    break;
                }
            }
        }
    }

    private boolean retry() {
        return InputView.input(RETRY_MESSAGE).equalsIgnoreCase(YES);
    }


    private Store openStore() {
        OutputView.printMessage(WELCOME_MESSAGE);
        List<Promotion> promotions = loadPromotions();
        Map<Product, Integer> products = loadProducts(promotions);

        return storeService.initializeStore(products);
    }

    private void processOrder(Store store) {
        OutputView.printProducts(store.getProducts());
        OrderResult orderResult = createOrderResult(store);
        processDiscounts(store, orderResult);

        int membershipDiscount = applyMembershipDiscount(orderResult);

        OutputView.printTotalQuantity(orderResult);
        OutputView.printPromotionQuantity(orderResult);
        printAmounts(orderResult, membershipDiscount);
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    private List<Promotion> loadPromotions() {
        List<String> inputPromotions = InputView.inputData(PROMOTION_FILE_PATH);
        return PromotionConverter.convertToPromotion(inputPromotions);
    }

    private Map<Product, Integer> loadProducts(List<Promotion> promotions) {
        List<String> inputProducts = InputView.inputData(PRODUCT_FILE_PATH);
        return ProductConverter.convertToProduct(inputProducts, promotions);
    }

    private OrderResult createOrderResult(Store store) {
        List<Order> orders = OrderConverter.convertToOrder(InputView.input(ORDER_MESSAGE));
        return storeService.processOrder(orders, store);
    }

    private void processDiscounts(Store store, OrderResult orderResult) {
        handleFreeItems(store, orderResult);
        checkNonAppliedPromotions(orderResult);

    }

    private void handleFreeItems(Store store, OrderResult orderResult) {
        List<Product> freeItems = storeService.checkEligibleFreeItems(store, orderResult);
        for (Product product : freeItems) {
            if (!promptFreeItemAddition(product, store, orderResult))
                freeItems.remove(product);
        }
    }

    private boolean promptFreeItemAddition(Product product, Store store, OrderResult orderResult) {
        String input = InputView.input(String.format(PROMOTION_MESSAGE_TEMPLATE, product.getName(), FREE_ITEM));
        if (input.equals(YES)) {
            storeService.getFreeItem(product, store, orderResult);
            return true;
        }
        return false;
    }

    private void checkNonAppliedPromotions(OrderResult orderResult) {
        Set<Product> products = orderResult.getOrderedProducts().keySet();
        for (Product product : products) {
            if (product.hasPromotion() && orderResult.hasPromotionAppliedForProductName(product.getName())) {
                promptPromotionAcceptance(product, orderResult);
            }
        }
    }

    private void promptPromotionAcceptance(Product product, OrderResult orderResult) {

        if (orderResult.calculatePromotionIsNotApplied(product) == ZERO){
            return;
        }

        String input = InputView.input(String.format(PROMOTION_IS_NOT_APPLY, product.getName(), orderResult.calculatePromotionIsNotApplied(product)));
        if (!input.equals(YES)) {
            throw new IllegalArgumentException(STOP_SHOPPING);
        }
    }

    private int applyMembershipDiscount(OrderResult orderResult) {
        if (isMembershipDiscountApplied() && orderResult.calculateDiscountAmount() == 0) {
            return storeService.getMembershipAmount(orderResult);
        }
        return ZERO;
    }

    private boolean isMembershipDiscountApplied() {
        return InputView.input(MEMBERSHIP_MESSAGE).equals(YES);
    }

    private void printAmounts(OrderResult orderResult, Integer membershipDiscount) {
        OutputView.printAmount(
                storeService.getTotalAmount(orderResult),
                storeService.getDiscountAmount(orderResult),
                membershipDiscount,
                storeService.getPayAmount(orderResult, membershipDiscount)
        );
    }
}
