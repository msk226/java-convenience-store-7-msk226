package store.controller;

import static store.utils.constant.ProductConstant.PRODUCT_FILE_PATH;
import static store.utils.constant.PromotionConstant.PROMOTION_FILE_PATH;
import static store.utils.message.ErrorMessage.*;
import static store.utils.message.InputMessage.*;
import static store.utils.message.OutputMessage.DIVISION;
import static store.utils.message.OutputMessage.WELCOME_MESSAGE;

import java.util.ArrayList;
import java.util.Iterator;
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
            }
        }
    }

    private boolean retry() {
        return InputView.input(RETRY_MESSAGE).equalsIgnoreCase(YES);
    }

    private boolean retryInputWithException(String message) {
        while (true) {
            try {
                String input = InputView.input(message);
                if (input.equalsIgnoreCase(YES) || input.equalsIgnoreCase(NO)) {
                    return input.equalsIgnoreCase(YES);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage()); // 예외 메시지 출력
            }
        }
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
        checkNonAppliedPromotions(store, orderResult);

    }

    private void handleFreeItems(Store store, OrderResult orderResult) {
        List<Product> freeItems = storeService.checkEligibleFreeItems(store, orderResult);
        Iterator<Product> iterator = freeItems.iterator();  // Iterator 생성

        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (!promptFreeItemAddition(product, store, orderResult)) {
                iterator.remove();  // Iterator의 remove() 메서드로 안전하게 삭제
            }
        }
    }


    private boolean promptFreeItemAddition(Product product, Store store, OrderResult orderResult) {
        boolean addFreeItem = retryInputWithException(String.format(PROMOTION_MESSAGE_TEMPLATE, product.getName(), FREE_ITEM));
        if (addFreeItem) {
            storeService.getFreeItem(product, store, orderResult);
        }
        return addFreeItem;
    }

    private void checkNonAppliedPromotions(Store store, OrderResult orderResult) {
        Set<Product> products = orderResult.getOrderedProducts().keySet();
        List<Product> productsToRemove = new ArrayList<>(); // 삭제할 항목을 위한 리스트

        for (Product product : products) {
            if (product.hasPromotion() && orderResult.hasPromotionAppliedForProductName(product.getName())) {
                if (!promptPromotionAcceptance(product, orderResult)) {
                    productsToRemove.add(product); // 삭제할 항목을 임시 저장
                }
            }
        }

        // 반복이 끝난 후 삭제 메서드 호출
        for (Product product : productsToRemove) {
            storeService.removeIfNoFreeItem(product, store, orderResult);
        }
    }
    private boolean promptPromotionAcceptance(Product product, OrderResult orderResult) {
        if (orderResult.calculatePromotionIsNotApplied(product) == ZERO) {
            return true;
        }

        return retryInputWithException(String.format(PROMOTION_IS_NOT_APPLY, product.getName(), orderResult.calculatePromotionIsNotApplied(product)));
    }




    private int applyMembershipDiscount(OrderResult orderResult) {
        if (isMembershipDiscountApplied()) {
            return storeService.getMembershipAmount(orderResult);
        }
        return ZERO;
    }

    private boolean isMembershipDiscountApplied() {
        return retryInputWithException(MEMBERSHIP_MESSAGE);
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
