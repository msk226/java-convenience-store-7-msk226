package store.model;

import java.util.List;

public class Payment {

    private final List<Order> successOrders;


    public Payment(List<Order> successOrders) {
        this.successOrders = successOrders;
    }

    public Integer getTotalAmount(){
        Integer totalAmount = 0;
        for (Order successOrder : successOrders){
            totalAmount += successOrder.getProduct().getPrice() * successOrder.getQuantity();
        }
        return totalAmount;
    }
}
