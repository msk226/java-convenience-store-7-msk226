package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentTest {

    private Order order;
    private Promotion promotion;
    private Product product;
    private Payment payment;
    @BeforeEach
    void setUp(){
        promotion = new Promotion("MD추천상품", 1, 1,
                LocalDate.of(2024,1,1), LocalDate.of(2024,12,31));
        product = new Product("사이다", 1_000, promotion);
        order = new Order(product, 100);
        payment = new Payment(new ArrayList<>(List.of(order)));
    }

    @Test
    void 결제_금액_계산(){
        Assertions.assertEquals(500_000, payment.getTotalAmount());
    }
}
