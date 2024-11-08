package store.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Inventory {
    private final Map<Product, Integer> standardStock = new HashMap<>();
    private final Map<Product, Integer> promotionStock = new HashMap<>();

    public void addProducts(Map<Product, Integer> newProducts){
        Set<Product> products = newProducts.keySet();
        for (Product product : products){
            Integer quantity = newProducts.get(product);

            if (product.hasPromotion()){
                addStock(promotionStock, product, quantity);
                continue;
            }
            addStock(standardStock, product, quantity);
        }
    }

    public void checkOrderIsPossible(List<Order> orders) {
        for (Order order : orders){
            int totalStockCount = getTotalStockCount(order.getProductName());
            validateStockAvailability(totalStockCount, order.getQuantity());
        }
    }


    public Map<Product, Integer> retrieveProductForOrder(List<Order> orders) {
        Map<Product, Integer> orderResult = new HashMap<>();

        for (Order order : orders){
            int remainingQuantity = order.getQuantity();

            // 프로모션 재고에서 먼저 차감
            remainingQuantity = processStock(order, orderResult, promotionStock, remainingQuantity);

            // 표준 재고에서 나머지 차감
            processOrderInStandardStock(order, remainingQuantity, orderResult);
        }

        return orderResult;
    }

    public void giveFreeItem(Product product, Integer quantity){
        reduceStock(promotionStock, product, quantity);
    }


    /*-----------------------------------------------------------------------------------------------------------------*/

    private void addStock(Map<Product, Integer> stock, Product product, Integer quantity) {
        stock.put(product, stock.getOrDefault(product, 0) + quantity);
    }

    private void validateStockAvailability(int totalStockCount, Integer orderQuantity) {
        if (totalStockCount < orderQuantity) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ENOUGH_STOCK);
        }
    }

    private void processOrderInStandardStock(Order order, int remainingQuantity, Map<Product, Integer> orderResult) {
        if (remainingQuantity > 0) {
            processStock(order, orderResult, standardStock, remainingQuantity);
        }
    }

    private int processStock(Order order, Map<Product, Integer> orderResult, Map<Product, Integer> stock, int remainingQuantity) {
        Product product = findProductByNameInStock(stock, order.getProductName());
        Integer stockCount = stock.get(product);

        if (stockCount >= remainingQuantity) {
            orderResult.put(product, remainingQuantity);
            reduceStock(stock, product, remainingQuantity);
            return 0;
        }

        orderResult.put(product, stockCount);
        reduceStock(stock, product, stockCount);
        return remainingQuantity - stockCount;
    }

    private Product findProductByNameInStock(Map<Product, Integer> stock, String productName) {
        for (Product product : stock.keySet()) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    private int getTotalStockCount(String productName) {
        Product productInPromotionStock = findProductByNameInStock(promotionStock, productName);
        Product productInStandardStock = findProductByNameInStock(standardStock, productName);

        if (productInPromotionStock == null && productInStandardStock == null){
            throw new IllegalArgumentException(ErrorMessage.NON_EXIST_PRODUCT);
        }

        return standardStock.getOrDefault(productInStandardStock, 0) +
                promotionStock.getOrDefault(productInPromotionStock, 0);
    }

    private void reduceStock(Map<Product, Integer> stock, Product product, int quantity) {
        int currentStock = getCurrentStock(stock, product, quantity);
        stock.put(product, currentStock - quantity);
    }

    private int getCurrentStock(Map<Product, Integer> stock, Product product, int quantity) {
        int currentStock = stock.getOrDefault(product, 0);
        validateStockAvailability(currentStock, quantity);
        return currentStock;
    }
}
