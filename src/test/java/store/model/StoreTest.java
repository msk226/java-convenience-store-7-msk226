package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class StoreTest {

    private Store store;
    private Promotion promotion;
    private Product productA;
    private Product productB;
    private Order order1;
    private Order order2;

    @BeforeEach
    public void setUp() {
        // Promotion 설정
        promotion = new Promotion("2+1", 3, 1, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));

        // Product 설정
        productA = new Product("Product A", 100, promotion);
        productB = new Product("Product B", 200, null);

        // Order 설정
        order1 = new Order(productA, 3); // 2+1 프로모션 적용
        order2 = new Order(productB, 2); // 프로모션 없음

        // Store 설정
        Inventory inventory = new Inventory();
        inventory.addStock(productA, 50);
        inventory.addStock(productB, 50);
        store = new Store(inventory);
    }

    @Test
    void 재고에_존재하지_않는_상품은_주문할_수_없다(){
        Product invalidProduct = new Product("TEST", 10_000, promotion);
        Order order = new Order(invalidProduct, 100);;
        assertThrows(IllegalArgumentException.class,
                () -> store.processOrder(new ArrayList<>(List.of(order))));
    }

    @Test
    void 재고_수량보다_많은_갯수의_주문은_처리할_수_없다(){
        Order order = new Order(productA, 100);;
        assertThrows(IllegalArgumentException.class,
                () ->  store.processOrder(new ArrayList<>(List.of(order))));
    }

    @Test
    public void 총_계산_금액_테스트() {
        int totalAmount = store.calculateTotalAmount(List.of(order1, order2));
        int expectedTotalAmount = order1.getTotalAmount() + order2.getTotalAmount();
        assertEquals(expectedTotalAmount, totalAmount);
    }

    @Test
    public void 총_할인_금액_테스트() {
        int discountAmount = store.calculateDiscountAmount(List.of(order1, order2));
        int expectedDiscountAmount = order1.getDiscountAmount() + order2.getDiscountAmount();
        assertEquals(expectedDiscountAmount, discountAmount);
    }

    @Test
    public void 최종_결제_금액_테스트() {
        int finalAmount = store.calculateFinalAmount(List.of(order1, order2));
        int expectedFinalAmount = (order1.getTotalAmount() + order2.getTotalAmount())
                - (order1.getDiscountAmount() + order2.getDiscountAmount());
        assertEquals(expectedFinalAmount, finalAmount);
    }
}
