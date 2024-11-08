package store.service;

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

    public Store initializeStore(Map<Product, Integer> products){
        Inventory inventory = new Inventory();
        inventory.addProducts(products);
        return new Store(inventory);
    }

    public Map<Product, Integer> processOrder(List<Order> orders, Store store) {
        return store.processOrder(orders);
    }

    //TODO 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.-> 얘도 뭐 대충은 된 듯
    //TODO 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
    //TODO 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다. -> 얘도 거의 다 된 듯
    //TODO 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다. -> 얘도 금방 됨

    public List<Product> checkEligibleFreeItems(Map<Product, Integer> orderResults, Store store){
        List<Product> promotionProduct = new ArrayList<>();

        Set<Product> products = orderResults.keySet();
        for (Product product : products){
            if (store.checkEligibleFreeItems(product, orderResults.get(product))){
                promotionProduct.add(product);
            }
        }
        return promotionProduct;
    }

    public Map<Product, Integer> getFreeItem(Map<Product, Integer> orderResult, Product product, Store store){
        return store.getFreeItem(orderResult, product, 1);
    }
}
