package store.model;

import static org.junit.jupiter.api.Assertions.*;

import camp.nextstep.edu.missionutils.test.NsTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class PromotionTest {
    private final String NAME = "MD추천상품";
    private final Integer BUY_AMOUNT = 1;
    private final Integer GET_AMOUNT = 1;
    private final LocalDate START_DATE = LocalDate.of(2024,1,1);
    private final LocalDate END_DATE = LocalDate.of(2024,12,31);


    @Test
    void 프로모션은_필수_속성을_가져야_한다(){
        Promotion promotion = new Promotion(NAME, BUY_AMOUNT, GET_AMOUNT, START_DATE, END_DATE);

        assertEquals(promotion.getName(), NAME);
        assertEquals(promotion.getBuyAmount(), BUY_AMOUNT);
        assertEquals(promotion.getGetAmount(), GET_AMOUNT);
        assertEquals(promotion.getStartDate(), START_DATE);
        assertEquals(promotion.getEndDate(), END_DATE);
    }

    @Test
    void 프로모션의_구매수량은_항상_0보다_크거나_같아야한다(){
        assertThrows(IllegalArgumentException.class,
                () -> new Promotion(NAME, -10, GET_AMOUNT, START_DATE, END_DATE));
    }
    @Test
    void 프로모션의_증정수량은_항상_0보다_크거나_같아야한다() {
        assertThrows(IllegalArgumentException.class,
                () -> new Promotion(NAME, BUY_AMOUNT, -10, START_DATE, END_DATE));
    }
    @Test
    void 프로모션의_시작날짜는_종료날짜_이전이어야_한다(){
        assertThrows(IllegalArgumentException.class,
                () -> new Promotion(NAME, BUY_AMOUNT, GET_AMOUNT, LocalDate.MAX, END_DATE));
    }
    @Test
    void 프로모션의_종료날짜는_시작날짜_이후여야_한다(){
        assertThrows(IllegalArgumentException.class,
                () -> new Promotion(NAME, BUY_AMOUNT, GET_AMOUNT, START_DATE, LocalDate.MIN));
    }

    @Test
    void 프로모션_할인_가격_계산(){
        Promotion promotion = new Promotion(NAME, BUY_AMOUNT, GET_AMOUNT, START_DATE, END_DATE);
        assertEquals(500_000, promotion.calculateDiscount(1_000, 1_000, LocalDate.now()));
    }

    @Test
    void 프로모션_기간이_아니면_프로모션을_적용하지_않고_계산(){
        Promotion promotion = new Promotion(NAME, BUY_AMOUNT, GET_AMOUNT, START_DATE, END_DATE);
        assertEquals(0, promotion.calculateDiscount(1_000, 1_000, LocalDate.MAX));
    }

}
