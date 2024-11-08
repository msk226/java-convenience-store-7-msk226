package store.model;

public class Product {
    private final String name;
    private final Integer price;
    private final Promotion promotion;

    public Product(String name, Integer price, Promotion promotion){
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.promotion = promotion;
    }

    public boolean hasPromotion(){
        return promotion == null;
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
    private void validatePrice(Integer price){
        if (price <= 0){
            throw new IllegalArgumentException("[ERROR] 상품의 가격은 항상 0보다 커야 합니다.");
        }
    }
}
