package store.model;


import static store.utils.message.ErrorMessage.INVALID_PROMOTION_DATE;
import static store.utils.message.ErrorMessage.PROMOTION_BUY_AMOUNT_GREATER_THAN_ZERO;
import static store.utils.message.ErrorMessage.PROMOTION_GET_AMOUNT_GREATER_THAN_ZERO;

import java.time.LocalDate;

public class Promotion {

    private final String name;
    private final Integer buyAmount;
    private final Integer getAmount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, Integer buyAmount, Integer getAmount, LocalDate startDate, LocalDate endDate) {
        validatePromotion(buyAmount, getAmount, startDate, endDate);
        this.name = name;
        this.buyAmount = buyAmount;
        this.getAmount = getAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validatePromotion(Integer buyAmount, Integer getAmount, LocalDate startDate, LocalDate endDate){
        validatePromotionBuy(buyAmount);
        validatePromotionGet(getAmount);
        validatePromotionDate(startDate, endDate);
    }

    private void validatePromotionBuy(Integer buyAmount){
        if (buyAmount < 0) {
            throw new IllegalArgumentException(PROMOTION_BUY_AMOUNT_GREATER_THAN_ZERO);
        }
    }

    private void validatePromotionGet(Integer getAmount){
        if (getAmount < 0) {
            throw new IllegalArgumentException(PROMOTION_GET_AMOUNT_GREATER_THAN_ZERO);
        }
    }

    private void validatePromotionDate(LocalDate startDate, LocalDate endDate){
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(INVALID_PROMOTION_DATE);
        }
    }
    public String getName() {
        return name;
    }

    public Integer getBuyAmount() {
        return buyAmount;
    }

    public Integer getGetAmount() {
        return getAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}
