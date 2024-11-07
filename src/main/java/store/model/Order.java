package store.model;

import static store.utils.ErrorMessage.ORDER_QUANTITY_GREATER_THAN_ZERO;

public class Order {

    private final Product product;
    private final Integer quantity;

    public Order(Product product, Integer quantity){
        validateOrder(quantity);
        this.product = product;
        this.quantity = quantity;
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
