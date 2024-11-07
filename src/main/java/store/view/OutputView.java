package store.view;

import static store.utils.message.OutputMessage.*;

import java.util.List;
import java.util.Set;
import store.model.Inventory;
import store.model.Product;
import store.utils.message.OutputMessage;

public class OutputView {

    public static void printWelcomeMessage(){
        System.out.println(WELCOME_MESSAGE);
    }

    public static void printProducts(Set<Product> products){
        for (Product product : products){
            System.out.println(product.getName() + BLANK + product.getPrice() +
                    product.getPromotion().getName());
        }

        System.out.println(ORDER_MESSAGE);
    }
}
