package store.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.model.Inventory;
import store.model.Order;
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

    public Map<Product, Integer> processOrder(List<Order> orders, Store store) {
        return store.processOrder(orders);
    }

    //TODO 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
    //TODO 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다. -> 얘도 거의 다 된 듯
    //TODO 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다. -> 얘도 금방 됨

    public List<Product> checkEligibleFreeItems(Store store){
        List<Product> promotionProduct = new ArrayList<>();

        Map<Product, Integer> orderResults = store.getOrderResult();

        Set<Product> products = orderResults.keySet();
        for (Product product : products){
            if (store.checkEligibleFreeItems(product, orderResults.get(product))){
                promotionProduct.add(product);
            }
        }
        return promotionProduct;
    }

    public Map<Product, Integer> getFreeItem(Product product, Store store){
        Map<Product, Integer> orderResults = store.getOrderResult();
        return store.getFreeItem(orderResults, product, FREE_ITEM);
    }

    public int countPromotionDiscount(Product product, Integer quantity){
        return product.getPromotion().countPromotionAmount(quantity);
    }

    public int getDiscountAmount(Map<Product, Integer> orderResult, Store store){
        return store.calculateDiscountAmount(orderResult, LocalDate.now());
    }
    public int getMembershipAmount(Map<Product, Integer> orderResult, Store store){
        return store.calculateMembershipAmount(orderResult);
    }
    public int getTotalAmount(Map<Product, Integer> orderResult, Store store){
        return store.calculateTotalAmount(orderResult);
    }

}
