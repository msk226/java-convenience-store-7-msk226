package store.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Inventory {

    private static final Integer ZERO = 0;
    private static final Integer FREE_ITEM = 1;

    private final Map<Product, Integer> standardStock = new LinkedHashMap<>();

    private final Map<Product, Integer> promotionStock = new LinkedHashMap<>();


    /* -------------------------------------------------------------------------------------------------------------------*/

    // 프로모션 재고에서 구매하지 않은 수량을 원래 재고로 돌리는 메서드
    public void returnUnpurchasedPromotionItems(Product product, int unpurchasedQuantity) {
        if (product.hasPromotion()) {
            addStock(promotionStock, product, unpurchasedQuantity);
        } else {
            addStock(standardStock, product, unpurchasedQuantity);
        }
    }

    protected Map<Product, Integer> getPromotionStock() {
        return promotionStock;
    }

    protected Map<Product, Integer> getStandardStock() {
        return standardStock;
    }


    public Map<Product, Integer> getStock() {
        Map<Product, Integer> combinedStock = new HashMap<>(standardStock);

        // 프로모션 재고가 있으면 기존 재고에 더하기
        for (Map.Entry<Product, Integer> entry : promotionStock.entrySet()) {
            combinedStock.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        return combinedStock;
    }

    // 재고 채우기
    public void addProducts(Map<Product, Integer> newProducts) {
        Set<Product> products = newProducts.keySet();
        for (Product product : products) {
            Integer quantity = newProducts.get(product);

            if (product.hasPromotion()) {
                addStock(promotionStock, product, quantity);
                continue;
            }
            addStock(standardStock, product, quantity);
        }
    }

    // 주문이 가능한지 판단하는 메서드,
    public void checkOrderIsPossible(List<Order> orders) {
        for (Order order : orders) {
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
            remainingQuantity = processPromotionStock(order, orderResult, nonPromotionResult, remainingQuantity);

            // 표준 재고에서 나머지 차감
            processOrderInStandardStock(order, remainingQuantity, orderResult);
        }

        // nonPromotionResult에 있는 재고를 orderResult에 추가
        addNonPromotionStockToOrderResult(nonPromotionResult, orderResult);

        return orderResult;
    }

    // 프로모션 재고에서 차감하는 메서드
    private int processPromotionStock(Order order, Map<Product, Integer> orderResult,
                                      Map<Product, Integer> nonPromotionResult, int remainingQuantity) {
        // 프로모션 재고가 존재하는지 확인
        Product promotionProduct = findProductByNameInStock(promotionStock, order.getProductName());
        if (promotionProduct != null) {
            remainingQuantity = processStockWithPromotion(order, orderResult, nonPromotionResult, remainingQuantity);
        }
        return remainingQuantity;
    }


    // nonPromotionResult에 있는 재고를 orderResult에 추가하는 메서드
    private void addNonPromotionStockToOrderResult(Map<Product, Integer> nonPromotionResult,
                                                   Map<Product, Integer> orderResult) {
        for (Map.Entry<Product, Integer> entry : nonPromotionResult.entrySet()) {
            orderResult.put(entry.getKey(), orderResult.getOrDefault(entry.getKey(), ZERO) + entry.getValue());
        }
    }



    public boolean isEligibleFreeItems(Product product, Integer orderAmount) {
        Promotion promotion = product.getPromotion();
        Integer buyAmount = product.getPromotion().getBuyAmount();
        Integer getAmount = product.getPromotion().getGetAmount();

        // 프로모션을 적용하려면 필요한 총 재고량
        int totalRequiredStock = promotion.countPromotionAmount(orderAmount) * (buyAmount + getAmount);

        // 잉여 주문량 처리 (주문량이 나누어떨어지지 않는 경우)
        if (promotion.countEligibleFreeItems(orderAmount) != 0) {
            totalRequiredStock += (promotion.countEligibleFreeItems(orderAmount));
        }

        Integer stock = promotionStock.get(product);

        // 재고가 필요한 만큼 충분한지 확인
        return stock >= totalRequiredStock;
    }


    // 수령 하지 않은 프로모션 아이템 제공
    public OrderResult giveFreeItem(OrderResult orderResult, Product product, Integer quantity) {
        reduceStock(promotionStock, product, quantity);
        orderResult.updateOrderedProducts(product, FREE_ITEM);
        return orderResult;
    }

    /*-----------------------------------------------------------------------------------------------------------------*/

    protected void addStock(Map<Product, Integer> stock, Product product, Integer quantity) {
        stock.put(product, stock.getOrDefault(product, ZERO) + quantity);
    }

    private void validateStockAvailability(int totalStockCount, Integer orderQuantity) {
        if (totalStockCount < orderQuantity) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ENOUGH_STOCK);
        }
    }

    // 프로모션 재고 처리 메서드
    private int processStockWithPromotion(Order order, Map<Product, Integer> orderResult,
                                          Map<Product, Integer> nonPromotionResult, int remainingQuantity) {
        Product product = findProductByNameInStock(promotionStock, order.getProductName());
        Integer stockCount = promotionStock.get(product);

        // 프로모션이 적용 가능한 수량을 계산
        int applicablePromotionQuantity = calculateApplicablePromotionQuantity(product, stockCount);

        // 프로모션이 적용 가능한 수량을 처리
        remainingQuantity = processPromotionStock(order, orderResult, product, applicablePromotionQuantity, remainingQuantity);

        // 남은 수량을 비프로모션 재고로 처리
        if (remainingQuantity > ZERO) {
            remainingQuantity = processNonPromotionStock(nonPromotionResult, product, stockCount, applicablePromotionQuantity, remainingQuantity);
        }

        return remainingQuantity;
    }

    // 프로모션 재고에서 적용 가능한 수량을 처리하는 메서드
    private int processPromotionStock(Order order, Map<Product, Integer> orderResult,
                                      Product product, int applicablePromotionQuantity, int remainingQuantity) {
        // 프로모션 수량을 주문 결과에 추가하고, 재고에서 차감
        if (applicablePromotionQuantity > ZERO) {
            int promotionAppliedQuantity = Math.min(applicablePromotionQuantity, remainingQuantity);
            orderResult.put(product, promotionAppliedQuantity);
            reduceStock(promotionStock, product, promotionAppliedQuantity);
            remainingQuantity -= promotionAppliedQuantity;
        }
        return remainingQuantity;
    }

    // 비프로모션 재고를 처리하는 메서드
    private int processNonPromotionStock(Map<Product, Integer> nonPromotionResult, Product product,
                                         Integer stockCount, int applicablePromotionQuantity, int remainingQuantity) {
        // 비프로모션 수량을 처리
        if (remainingQuantity > ZERO) {
            int nonPromotionApplicableQuantity = Math.min(stockCount - applicablePromotionQuantity, remainingQuantity);
            nonPromotionResult.put(product, nonPromotionApplicableQuantity);
            reduceStock(promotionStock, product, nonPromotionApplicableQuantity);
            remainingQuantity -= nonPromotionApplicableQuantity;
        }
        return remainingQuantity;
    }


    private int calculateApplicablePromotionQuantity(Product product, Integer stockCount) {
        if (!product.hasPromotion()) {
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

    private int processStock(Order order, Map<Product, Integer> orderResult, Map<Product, Integer> stock,
                             int remainingQuantity) {
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

        if (productInPromotionStock == null && productInStandardStock == null) {
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
