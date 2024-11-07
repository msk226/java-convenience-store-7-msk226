package store.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void 상품의_가격은_항상_양수여야_한다(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Product("콜라", -1000, new Promotion("name", 1, 1, LocalDate.MIN, LocalDate.MAX)));
    }
}
