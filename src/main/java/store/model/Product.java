package store.model;

import store.utils.message.ErrorMessage;

public class Product {

    private static final Integer ZERO = 0;

    private final String name;
    private final Integer price;
    private final Promotion promotion;

    public Product(String name, Integer price, Promotion promotion) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.promotion = promotion;
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    private void validatePrice(Integer price) {
        if (price <= ZERO) {
            throw new IllegalArgumentException(ErrorMessage.PRODUCT_PRICE_GREATER_THAN_ZERO);
        }
    }
}
