package store.view;

import static store.utils.message.OutputMessage.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.model.Inventory;
import store.model.Order;
import store.model.Product;
import store.model.Store;
import store.utils.message.OutputMessage;

public class OutputView {

    public static void printWelcomeMessage(){
        System.out.println(WELCOME_MESSAGE);
    }

    public static void printTotalQuantity(List<Order> orders, Store store){
        printReceiptWelcomeMessage();
        for (Order order : orders){
            System.out.println(String.format(PRODUCT_MESSAGE, order.getProductName(),
                    order.getQuantity(), store.getPrice(order.getProductName()) * order.getQuantity()));
        }
    }

    public static void printPromotionQuantity(String productName,int promotionQuantity){
        System.out.printf(PROMOTION, productName, promotionQuantity);
    }

    public static void printAmount(int totalAmount, int discountAmount, int membershipAmount, int payAmount){
        System.out.printf(TOTAL_AMOUNT, totalAmount);
        System.out.printf(DISCOUNT_AMOUNT, discountAmount);
        System.out.printf(MEMBERSHIP_AMOUNT, membershipAmount);
        System.out.printf(PAY_AMOUNT, payAmount);
        System.out.println(DIVISION);

    }

    public static void printProducts(Map<Product, Integer> products){
        Set<Product> productsKeys = products.keySet();
        for (Product product : productsKeys){

            String promotionName = DEFAULT_PROMOTION_NAME;

            if (product.hasPromotion()){
                promotionName = product.getPromotion().getName();
            }

            System.out.println(product.getName() + TAB + getFormattedPrice(product.getPrice()) + TAB +
                    products.get(product) + COUNT + TAB +  promotionName);
        }
        System.out.println(BLANK);
    }


    private static String getFormattedPrice(Integer price){
        DecimalFormat decimalFormat = new DecimalFormat(COUNT_FORMAT);
        return decimalFormat.format(price) + WON;
    }

    private static void printReceiptWelcomeMessage(){
        System.out.println(RECEIPT_WELCOME_MESSAGE);
        System.out.println(RECEIPT_TITLE_MESSAGE);
    }

}
