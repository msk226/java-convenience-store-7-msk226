package store.model;

import static store.utils.message.ErrorMessage.ORDER_QUANTITY_GREATER_THAN_ZERO;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {

    private static final Integer ZERO = 0;

    private final String productName;
    private final Integer quantity;
    private final LocalDate orderDate;

    public Order(String productName, Integer quantity) {
        validateOrder(quantity);
        this.productName = productName;
        this.quantity = quantity;
        this.orderDate = DateTimes.now().toLocalDate();
    }


    /* -------------------------------------------------------------------------------------------------------------------*/

    public String getProductName() {
        return productName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Integer getQuantity() {
        return quantity;
    }


    /* -------------------------------------------------------------------------------------------------------------------*/

    private void validateOrder(Integer quantity) {
        validateOrderQuantity(quantity);
    }

    private void validateOrderQuantity(Integer quantity) {
        if (quantity <= ZERO) {
            throw new IllegalArgumentException(ORDER_QUANTITY_GREATER_THAN_ZERO);
        }
    }

}
