package store.model;

import static store.utils.constant.OrderConstant.*;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.utils.constant.OrderConstant;

public class OrderResult {

    private static final Integer ZERO = 0;
    private static final Double MEMBERSHIP = 0.30;

    private final Map<Product, Integer> orderedProducts;
    private final LocalDate orderDate;

    public OrderResult(Map<Product, Integer> orderedProducts) {
        this.orderedProducts = orderedProducts;
        this.orderDate = DateTimes.now().toLocalDate();
    }

    /* -------------------------------------------------------------------------------------------------------------------*/
    public void initializeOrderResult() {
        Set<Product> products = orderedProducts.keySet();
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (orderedProducts.get(product).equals(ZERO)) {
                iterator.remove(); // Safely removes the product during iteration
            }
        }
    }

    public Map<Product, Integer> updateOrderedProducts(Product product, Integer quantity) {
        Integer existQuantity = orderedProducts.get(product);
        orderedProducts.put(product, existQuantity + quantity);
        return orderedProducts;
    }

    public boolean isPromotionApplied(){
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            if (product.hasPromotion() && product.getPromotion().isValidPromotion(orderDate)){
                return true;
            }
        }
        return false;
    }

    public Product findNonPromotionProductByProductName(String productName){
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            if (product.getName().equals(productName) && !product.hasPromotion()){
                return product;
            }
        }
        return null;
    }

    public Product findPromotionProductByProductName(String productName){
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            if (product.getName().equals(productName) && product.hasPromotion()){
                return product;
            }
        }
        return null;
    }

    public Map<Product, Integer> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getQuantity(Product product) {
        return orderedProducts.getOrDefault(product, 0);
    }

    public boolean hasPromotionAppliedForProductName(String productName) {
        for (Product product : orderedProducts.keySet()) {
            if (product.getName().equals(productName) && product.getPromotion() != null) {
                return true;
            }
        }
        return false;
    }


    public int calculateTotalAmount() {
        int totalAmount = ZERO;
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products) {
            totalAmount += product.getPrice() * orderedProducts.get(product);
        }
        return totalAmount;
    }

    public int calculateDiscountAmount() {
        int totalDiscountAmount = ZERO;
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products) {
            Promotion promotion = product.getPromotion();
            if (promotion != null) {
                totalDiscountAmount += promotion.calculateDiscount(orderedProducts.get(product), product.getPrice(),
                        orderDate);
            }
        }
        return totalDiscountAmount;
    }

    public int calculateMembershipAmount() {
        int totalNonPromotedPrice;
        int totalDiscountPrice = 0;
        Set<Product> products = orderedProducts.keySet();
        for (Product product : products){
            totalDiscountPrice += calculatePromotionBonusQuantity(product) * product.getPrice();
            if ((product.hasPromotion() && product.getPromotion().isValidPromotion(orderDate)) && totalDiscountPrice == calculateDiscountAmount()){
                int discountAmount = product.getPromotion().countEligibleFreeItems(orderedProducts.get(product));
                return (int) (discountAmount * product.getPrice() * MEMBERSHIP);
            }
        }

        totalNonPromotedPrice = calculateTotalAmount() - calculateDiscountAmount();
        int membershipDiscountAmount = (int) (totalNonPromotedPrice * MEMBERSHIP);

        if (membershipDiscountAmount >= MAX_DISCOUNT_AMOUNT) {
            return MAX_DISCOUNT_AMOUNT;
        }
        return membershipDiscountAmount;
    }

    private int updateTotalNonPromotedPrice(Product product, List<String> alreadyCheckedProductName,
                                         int totalNonPromotedPrice) {
        if (!alreadyCheckedProductName.contains(product.getName())){
            if (product.hasPromotion() && product.getPromotion().isValidPromotion(orderDate)) {
                totalNonPromotedPrice += calculatePromotionIsNotAppliedForMembership(product) * product.getPrice();
                alreadyCheckedProductName.add(product.getName());
                return totalNonPromotedPrice;
            }
            totalNonPromotedPrice += product.getPrice() * orderedProducts.get(product);
        }
        return totalNonPromotedPrice;
    }

    public int calculateFinalAmount(Integer membershipDiscount) {
        return calculateTotalAmount() - (calculateDiscountAmount() + membershipDiscount);
    }

    public int calculatePromotionBonusQuantity(Product product) {
        if (!product.hasPromotion()) {
            return 0;
        }
        int quantity = orderedProducts.get(product);
        return product.getPromotion().countPromotionAmount(quantity);
    }

    public int getQuantityByProductName(String productName) {
        Set<Product> products = orderedProducts.keySet();
        int total = 0;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                total += orderedProducts.get(product);
            }
        }
        return total;
    }


    public int calculatePromotionIsNotApplied(Product product) {
        Integer stock = orderedProducts.get(product);
        if (!isPromotionApplied() && product.getPromotion().countPromotionAmount(stock) < 1){
            return 0;
        }
        Promotion promotion = product.getPromotion();

        return getQuantityByProductName(product.getName()) - (calculatePromotionBonusQuantity(product)
                * (promotion.getBuyAmount() + promotion.getGetAmount()));
    }

    public int calculatePromotionIsNotAppliedForMembership(Product product) {
        Integer stock = orderedProducts.get(product);
        if (!isPromotionApplied() || product.getPromotion().countPromotionAmount(stock) >= 1){
            return 0;
        }
        Promotion promotion = product.getPromotion();

        return getQuantityByProductName(product.getName()) - (calculatePromotionBonusQuantity(product)
                * (promotion.getBuyAmount() + promotion.getGetAmount()));
    }


    public LocalDate getOrderDate() {
        return orderDate;
    }

    /* -------------------------------------------------------------------------------------------------------------------*/
}
