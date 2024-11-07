package store.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StoreTest {

    private Inventory inventory;
    private Promotion promotion;
    private Product product;
    private Store store;
    @BeforeEach
    void setUp(){
        promotion = new Promotion("MD추천상품", 1, 1,
                LocalDate.of(2024,1,1), LocalDate.of(2024,12,31));
        product = new Product("사이다", 1_000, promotion);
        inventory = new Inventory();
        inventory.addStock(product, 10);

        store = new Store(inventory);
    }

    @Test
    void 재고에_존재하지_않는_상품은_주문할_수_없다(){
        Product invalidProduct = new Product("TEST", 10_000, promotion);
        Order order = new Order(invalidProduct, 100);;
        Assertions.assertThrows(IllegalArgumentException.class,
                store.processOrder(order));
    }

    @Test
    void 재고_수량보다_많은_갯수의_주문은_처리할_수_없다(){
        Order order = new Order(product, 100);;
        Assertions.assertThrows(IllegalArgumentException.class,
                store.processOrder(order));
    }



}
