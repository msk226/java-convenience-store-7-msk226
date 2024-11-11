package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Store {

    private final Inventory inventory;

    public Store(Inventory inventory) {
        this.inventory = inventory;
    }

    /* -------------------------------------------------------------------------------------------------------------------*/

    public Map<Product, Integer> getProducts() {
        return inventory.getStock();
    }

    public OrderResult processOrder(List<Order> orders) {
        inventory.checkOrderIsPossible(orders);
        return new OrderResult(inventory.retrieveProductForOrder(orders));
    }

    public boolean checkEligibleFreeItems(Product product, Integer quantity) {
        if (!product.hasPromotion() || !inventory.isEligibleFreeItems(product, quantity)) {
            return false;
        }
        return product.getPromotion().checkEligibleFreeItems(quantity);
    }

    public OrderResult getFreeItem(OrderResult orderResult, Product product, Integer quantity) {
        return inventory.giveFreeItem(orderResult, product, quantity);
    }

    public void removeIfNoFreeItem(Product removeProduct, OrderResult orderResult) {
        int nonAppliedPromotionCount = orderResult.calculatePromotionIsNotApplied(removeProduct);

        Product nonPromotionProductByProductName = orderResult.findNonPromotionProductByProductName(
                removeProduct.getName());

        nonAppliedPromotionCount = updateNonAppliedPromotionCount(orderResult, nonPromotionProductByProductName,
                nonAppliedPromotionCount);

        if (nonAppliedPromotionCount <= 0){
            return;
        }
        Product promotionProduct = orderResult.findPromotionProductByProductName(removeProduct.getName());
        inventory.returnUnpurchasedPromotionItems(promotionProduct, nonAppliedPromotionCount);
        orderResult.updateOrderedProducts(promotionProduct, nonAppliedPromotionCount * -1);
    }

    private int updateNonAppliedPromotionCount(OrderResult orderResult, Product nonPromotionProductByProductName,
                                            int nonAppliedPromotionCount) {
        Integer orderQuantity = orderResult.getOrderedProducts().get(nonPromotionProductByProductName);
        int min = Math.min(orderQuantity, nonAppliedPromotionCount);
        inventory.returnUnpurchasedPromotionItems(nonPromotionProductByProductName, min);
        orderResult.updateOrderedProducts(nonPromotionProductByProductName, min * -1);
        nonAppliedPromotionCount -= orderQuantity;
        return nonAppliedPromotionCount;
    }
    /* --------------------------------------------------------------------------------------------*/

    // 0, 7

    // 8, 7
}
