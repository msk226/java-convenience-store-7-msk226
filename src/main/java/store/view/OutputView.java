package store.view;

import static store.utils.message.OutputMessage.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.model.Inventory;
import store.model.Product;
import store.utils.message.OutputMessage;

public class OutputView {

    public static void printWelcomeMessage(){
        System.out.println(WELCOME_MESSAGE);
    }

    public static void printProducts(Map<Product, Integer> products){
        Set<Product> productsKeys = products.keySet();
        for (Product product : productsKeys){

            String promotionName = "null";

            if (product.hasPromotion()){
                promotionName = product.getPromotion().getName();
            }

            System.out.println(product.getName() + TAB + getFormattedPrice(product.getPrice()) + TAB +
                    products.get(product) + COUNT + TAB +  promotionName);
        }
        System.out.println(BLANK);
    }


    private static String getFormattedPrice(Integer price){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(price) + "원";
    }

}
