//package store.model;
//
//import java.time.LocalDate;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class InventoryTest {
//    private Inventory inventory;
//    private Product product;
//
//    @BeforeEach
//    void setUp(){
//        inventory = new Inventory();
//        product = new Product("Soda", 1500, new Promotion("탄산2+1", 1, 1, LocalDate.MIN, LocalDate.MAX));
//    }
//
//
//    @Test
//    void 재고_조회_테스트() {
//        inventory.addStock(product, 5);
//        int stock = inventory.getStock(product);
//        assertEquals(5, stock, "상품의 재고가 예상과 일치하지 않습니다.");
//    }
//
//    @Test
//    void 재고_감소_테스트() {
//        inventory.addStock(product, 8);
//        inventory.reduceStock(product, 3);
//        assertEquals(5, inventory.getStock(product));
//    }
//
//    @Test
//    void 재고_부족_예외_테스트() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            inventory.reduceStock(product, 5);
//        });
//    }
//}
