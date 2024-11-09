package store.view;

import static store.utils.message.OutputMessage.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import store.model.Order;
import store.model.OrderResult;
import store.model.Product;
import store.model.Store;

public class OutputView {
    private static final Integer ZERO = 0;
    private static final Integer FREE_ITEM = 1;
    private static final Integer MINUS = -1;

    public static void printMessage(String message){
        System.out.println(message);
    }



    // 총합, 수량 출력
    public static void printTotalQuantity(OrderResult orderResult) {
        printReceiptWelcomeMessage();
        Map<Product, Integer> orderedProducts = orderResult.getOrderedProducts();

        // 상품별로 수량과 금액 합산
        Map<String, Integer> totalQuantityByProduct = calculateTotalQuantityByProduct(orderedProducts);
        Map<String, Integer> totalPriceByProduct = calculateTotalPriceByProduct(orderedProducts);

        // 합산된 수량과 금액 출력
        printSummary(totalQuantityByProduct, totalPriceByProduct);
    }

    // 상품별로 수량 합산
    private static Map<String, Integer> calculateTotalQuantityByProduct(Map<Product, Integer> orderedProducts) {
        Map<String, Integer> totalQuantityByProduct = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : orderedProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalQuantityByProduct.merge(product.getName(), quantity, Integer::sum);
        }
        return totalQuantityByProduct;
    }

    // 상품별로 금액 합산
    private static Map<String, Integer> calculateTotalPriceByProduct(Map<Product, Integer> orderedProducts) {
        Map<String, Integer> totalPriceByProduct = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : orderedProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            int totalPrice = product.getPrice() * quantity;
            totalPriceByProduct.merge(product.getName(), totalPrice, Integer::sum);
        }
        return totalPriceByProduct;
    }

    // 수량과 금액 출력
    private static void printSummary(Map<String, Integer> totalQuantityByProduct, Map<String, Integer> totalPriceByProduct) {
        for (String productName : totalQuantityByProduct.keySet()) {
            int totalQuantity = totalQuantityByProduct.get(productName);
            int totalPrice = totalPriceByProduct.get(productName);
            System.out.printf(PRODUCT_MESSAGE, productName, totalQuantity, totalPrice);
        }
    }



    // 프로모션 적용 수량 출력
    public static void printPromotionQuantity(OrderResult orderResult){
        printMessage(PROMOTION_DIVISION);
        Map<Product, Integer> orderedProducts = orderResult.getOrderedProducts();
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            if (product.hasPromotion()){
                System.out.printf(PROMOTION, product.getName(), orderResult.calculatePromotionBonusQuantity(product));
            }
        }
        printMessage(DIVISION);
    }

    // 총 구매액, 행사할인, 멤버십 할인, 실제 결제액 출력
    public static void printAmount(int totalAmount, int discountAmount, int membershipAmount, int payAmount){
        System.out.printf(TOTAL_AMOUNT, TOTAL_AMOUNT_LABEL, getFormattedPrice(totalAmount));
        System.out.printf(DISCOUNT_AMOUNT, DISCOUNT_AMOUNT_LABEL, getFormattedPrice(discountAmount * MINUS));
        System.out.printf(MEMBERSHIP_AMOUNT, MEMBERSHIP_AMOUNT_LABEL, getFormattedPrice(membershipAmount * MINUS));
        System.out.printf(PAY_AMOUNT, PAY_AMOUNT_LABEL,getFormattedPrice(payAmount));
        System.out.println(DIVISION);
    }

    public static void printProducts(Map<Product, Integer> products){
        Set<Product> productsKeys = products.keySet();
        for (Product product : productsKeys){
            String promotionName = DEFAULT_PROMOTION_NAME;

            if (product.hasPromotion()) {
                promotionName = product.getPromotion().getName();
            }
            String stock = EMPTY;
            if (products.get(product) != ZERO){
                stock = products.get(product) + COUNT;
            }

            System.out.println(PREFIX + product.getName() + TAB +
                    getFormattedPrice(product.getPrice()) + WON + TAB +
                    stock + TAB +
                    promotionName);
        }
        System.out.println(BLANK);
    }


    // 금액 포맷팅
    private static String getFormattedPrice(Integer price){
        DecimalFormat decimalFormat = new DecimalFormat(COUNT_FORMAT);
        return decimalFormat.format(price);
    }

    // 영수증 출력 헤더
    private static void printReceiptWelcomeMessage(){
        System.out.println(RECEIPT_WELCOME_MESSAGE);
        System.out.println(RECEIPT_TITLE_MESSAGE);
    }
}
