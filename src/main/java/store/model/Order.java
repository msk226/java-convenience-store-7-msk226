package store.model;

import static store.utils.message.ErrorMessage.ORDER_QUANTITY_GREATER_THAN_ZERO;

import java.time.LocalDate;

public class Order {

    private final Product product;
    private final Integer quantity;
    private final LocalDate orderDate;

    public Order(Product product, Integer quantity){
        validateOrder(quantity);
        this.product = product;
        this.quantity = quantity;
        this.orderDate = LocalDate.now();
    }

    public int getTotalAmount() {
        return product.getPrice() * quantity;
    }
    public int getDiscountAmount(){
        Promotion promotion = product.getPromotion();
        if (promotion == null){
            return 0;
        }
        return promotion.calculateDiscount(quantity, product.getPrice(), orderDate);
    }
    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }


    private void validateOrder(Integer quantity){
        validateOrderQuantity(quantity);
    }

    private void validateOrderQuantity(Integer quantity){
        if (quantity <= 0) {
            throw new IllegalArgumentException(ORDER_QUANTITY_GREATER_THAN_ZERO);
        }
    }

}
