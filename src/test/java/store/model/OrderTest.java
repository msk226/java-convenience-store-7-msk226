package store.model;

import static org.junit.jupiter.api.Assertions.*;

import camp.nextstep.edu.missionutils.test.NsTest;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest extends NsTest {

    private final Integer ORDER_QUANTITY = 10;
    private Promotion promotion;
    private Product product;

    @BeforeEach
    void before(){
        promotion = new Promotion("MD추천상품", 1, 1,
                LocalDate.of(2024,1,1), LocalDate.of(2024,12,31));
        product = new Product("사이다", 1_000, promotion);
    }
    @Test
    void 주문은_필수_속성을_가져야_한다(){
        Order order = new Order(product, ORDER_QUANTITY);

        assertEquals(order.getProduct().getName(), NAME);
        assertEquals(order.getQuantity(), ORDER_QUANTITY);
    }

    @Test
    void 주문_수량은_0보다_커야_한다(){
        Integer badOrderQuantity = -10;
        assertThrows(IllegalArgumentException.class,
                () -> new Order(product, badOrderQuantity));
    }



    @Override
    protected void runMain() {

    }
}
