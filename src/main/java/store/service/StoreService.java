package store.service;

import static store.utils.message.OutputMessage.PRODUCT_MESSAGE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.model.Inventory;
import store.model.Order;
import store.model.OrderResult;
import store.model.Product;
import store.model.Store;
import store.view.InputView;

public class StoreService {

    private static final Integer FREE_ITEM = 1;

    public Store initializeStore(Map<Product, Integer> products){
        Inventory inventory = new Inventory();
        inventory.addProducts(products);
        return new Store(inventory);
    }

    public OrderResult processOrder(List<Order> orders, Store store) {
        return store.processOrder(orders);
    }

    public List<Product> checkEligibleFreeItems(Store store, OrderResult orderResult){
        List<Product> promotionProduct = new ArrayList<>();

        Set<Product> products = orderResult.getOrderedProducts().keySet();
        for (Product product : products){
            if (store.checkEligibleFreeItems(product, orderResult.getQuantity(product))){
                promotionProduct.add(product);
            }
        }
        return promotionProduct;
    }

    public void getFreeItem(Product product, Store store, OrderResult orderResult){
        store.getFreeItem(orderResult, product, FREE_ITEM);
    }

    public int countPromotionDiscount(Product product, Integer quantity){
        return product.getPromotion().countPromotionAmount(quantity);
    }

    public int getTotalAmount(OrderResult orderResult){
        Map<Product, Integer> orderedProducts = orderResult.getOrderedProducts();
        Set<Product> products = orderedProducts.keySet();
        int totalPrice = 0;
        for (Product product : products){
            totalPrice += product.getPrice() * orderedProducts.get(product);
        }
        return totalPrice;
    }

    public int getDiscountAmount(OrderResult orderResult){
        return orderResult.calculateDiscountAmount(LocalDate.now());
    }
    public int getMembershipAmount(OrderResult orderResult){
        return orderResult.calculateMembershipAmount();
    }
    public int getPayAmount(OrderResult orderResult){
        return orderResult.calculateFinalAmount(LocalDate.now());
    }

}
