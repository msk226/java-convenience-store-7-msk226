package store.view;

import static store.utils.message.OutputMessage.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.model.Order;
import store.model.Product;
import store.model.Store;

public class OutputView {

    public static void printWelcomeMessage(){
        System.out.println(WELCOME_MESSAGE);
    }

    // 총합, 수량 출력
    public static void printTotalQuantity(List<Order> orders, Store store){
        printReceiptWelcomeMessage();
        for (Order order : orders){
            int totalPrice = store.getPrice(order.getProductName()) * order.getQuantity();
            System.out.printf(PRODUCT_MESSAGE, order.getProductName(), order.getQuantity(), totalPrice);
        }
    }

    // 프로모션 적용 수량 출력
    public static void printPromotionQuantity(String productName, int promotionQuantity){
        System.out.println(PROMOTION_DIVISION);
        System.out.printf(PROMOTION, productName, promotionQuantity);
    }

    // 총 구매액, 행사할인, 멤버십 할인, 실제 결제액 출력
    public static void printAmount(int totalAmount, int discountAmount, int membershipAmount, int payAmount){
        System.out.printf(TOTAL_AMOUNT, TOTAL_AMOUNT_LABEL, totalAmount, totalAmount);
        System.out.printf(DISCOUNT_AMOUNT, DISCOUNT_AMOUNT_LABEL, discountAmount, discountAmount);
        System.out.printf(MEMBERSHIP_AMOUNT, MEMBERSHIP_AMOUNT_LABEL, membershipAmount, membershipAmount);
        System.out.printf(PAY_AMOUNT, PAY_AMOUNT_LABEL, payAmount, payAmount);
        System.out.println(DIVISION);
    }

    public static void printProducts(Map<Product, Integer> products){
        Set<Product> productsKeys = products.keySet();
        for (Product product : productsKeys){
            String promotionName = DEFAULT_PROMOTION_NAME;
            if (product.hasPromotion()) {
                promotionName = product.getPromotion().getName();
            }
            System.out.println(PREFIX + product.getName() + TAB +
                    getFormattedPrice(product.getPrice()) + TAB +
                    products.get(product) + COUNT + TAB +
                    promotionName);
        }
        System.out.println(BLANK);
    }


    // 금액 포맷팅
    private static String getFormattedPrice(Integer price){
        DecimalFormat decimalFormat = new DecimalFormat(COUNT_FORMAT);
        return decimalFormat.format(price) + WON;
    }

    // 영수증 출력 헤더
    private static void printReceiptWelcomeMessage(){
        System.out.println(RECEIPT_WELCOME_MESSAGE);
        System.out.println(RECEIPT_TITLE_MESSAGE);
    }
}
