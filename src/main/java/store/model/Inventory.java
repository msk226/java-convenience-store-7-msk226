package store.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Inventory {

    private static final Integer ZERO = 0;
    private static final Integer FREE_ITEM = 1;

    private final Map<Product, Integer> standardStock = new HashMap<>();
    private final Map<Product, Integer> promotionStock = new HashMap<>();


    /* -------------------------------------------------------------------------------------------------------------------*/
    // 재고 채우기
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

    // 주문이 가능한지 판단하는 메서드,
    public void checkOrderIsPossible(List<Order> orders) {
        for (Order order : orders){
            int totalStockCount = getTotalStockCount(order.getProductName());
            validateStockAvailability(totalStockCount, order.getQuantity());
        }
    }

    // 상품의 가격을 가져오는 메서드
    public Integer getPrice(String productName) {
        Product productByNameInStock = findProductByNameInStock(standardStock, productName);
        if (productByNameInStock != null) {
            return productByNameInStock.getPrice();
        }

        Product productByNameInPromotionStock = findProductByNameInStock(promotionStock, productName);
        if (productByNameInPromotionStock != null) {
            return productByNameInPromotionStock.getPrice();
        }
        return ZERO;
    }


    // 주문 처리 로직
    public Map<Product, Integer> retrieveProductForOrder(List<Order> orders) {
        Map<Product, Integer> orderResult = new HashMap<>();
        Map<Product, Integer> nonPromotionResult = new HashMap<>();

        for (Order order : orders) {
            int remainingQuantity = order.getQuantity();

            // 프로모션 재고에서 먼저 차감
            if (findProductByNameInStock(promotionStock, order.getProductName()) != null) {
                remainingQuantity = processStockWithPromotion(order, orderResult, nonPromotionResult, remainingQuantity);
            }

            // 표준 재고에서 나머지 차감
            processOrderInStandardStock(order, remainingQuantity, orderResult);
        }

        // nonPromotionResult에 있는 재고를 orderResult에 추가
        for (Map.Entry<Product, Integer> entry : nonPromotionResult.entrySet()) {
            orderResult.put(entry.getKey(), orderResult.getOrDefault(entry.getKey(), ZERO) + entry.getValue());
        }

        return orderResult;
    }



    public boolean isEligibleFreeItems(Product product, Integer orderAmount){
        Integer buyAmount = product.getPromotion().getBuyAmount();
        Integer getAmount = product.getPromotion().getGetAmount();

        Integer stock = promotionStock.get(product);
        return stock >= (orderAmount / (buyAmount + getAmount));
    }


    // 수령 하지 않은 프로모션 아이템 제공
    public OrderResult giveFreeItem(OrderResult orderResult, Product product, Integer quantity){
        reduceStock(promotionStock, product, quantity);
        orderResult.updateOrderedProducts(product, FREE_ITEM);
        return orderResult;
    }

    /*-----------------------------------------------------------------------------------------------------------------*/

    private void addStock(Map<Product, Integer> stock, Product product, Integer quantity) {
        stock.put(product, stock.getOrDefault(product, ZERO) + quantity);
    }

    private void validateStockAvailability(int totalStockCount, Integer orderQuantity) {
        if (totalStockCount < orderQuantity) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ENOUGH_STOCK);
        }
    }
    private int processStockWithPromotion(Order order, Map<Product, Integer> orderResult, Map<Product, Integer> nonPromotionResult, int remainingQuantity) {
        Product product = findProductByNameInStock(promotionStock, order.getProductName());
        Integer stockCount = promotionStock.get(product);

        int applicablePromotionQuantity = calculateApplicablePromotionQuantity(product, stockCount);

        // 프로모션이 적용 가능한 수량을 orderResult에 추가하고, 재고에서 차감
        if (applicablePromotionQuantity > ZERO) {
            int promotionAppliedQuantity = Math.min(applicablePromotionQuantity, remainingQuantity);
            orderResult.put(product, promotionAppliedQuantity);
            reduceStock(promotionStock, product, promotionAppliedQuantity);
            remainingQuantity -= promotionAppliedQuantity;
        }

        // 남아 있는 수량을 nonPromotionResult에 추가하되, 재고가 충분한 범위 내에서 차감
        if (remainingQuantity > ZERO) {
            int nonPromotionApplicableQuantity = Math.min(stockCount - applicablePromotionQuantity, remainingQuantity);
            nonPromotionResult.put(product, nonPromotionApplicableQuantity);
            reduceStock(promotionStock, product, nonPromotionApplicableQuantity);
            remainingQuantity -= nonPromotionApplicableQuantity;
        }

        return remainingQuantity;
    }

    private int calculateApplicablePromotionQuantity(Product product, Integer stockCount) {
        if (!product.hasPromotion()){
            return ZERO;
        }
        int promotionCount = product.getPromotion().countTotalPromotionAmount(stockCount);
        return Math.min(promotionCount, stockCount);
    }

    private void processOrderInStandardStock(Order order, int remainingQuantity, Map<Product, Integer> orderResult) {
        if (remainingQuantity > ZERO) {
            processStock(order, orderResult, standardStock, remainingQuantity);
        }
    }

    private int processStock(Order order, Map<Product, Integer> orderResult, Map<Product, Integer> stock, int remainingQuantity) {
        Product product = findProductByNameInStock(stock, order.getProductName());
        Integer stockCount = stock.get(product);

        if (stockCount >= remainingQuantity) {
            orderResult.put(product, remainingQuantity);
            reduceStock(stock, product, remainingQuantity);
            return ZERO;
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

        return standardStock.getOrDefault(productInStandardStock, ZERO) +
                promotionStock.getOrDefault(productInPromotionStock, ZERO);
    }

    private void reduceStock(Map<Product, Integer> stock, Product product, int quantity) {
        int currentStock = getCurrentStock(stock, product, quantity);
        stock.put(product, currentStock - quantity);
    }

    private int getCurrentStock(Map<Product, Integer> stock, Product product, int quantity) {
        int currentStock = stock.getOrDefault(product, ZERO);
        validateStockAvailability(currentStock, quantity);
        return currentStock;
    }
}
