package store.model;

import java.time.LocalDate;

public class Promotion {

    private final String name;
    private final Integer buyAmount;
    private final Integer getAmount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, Integer buyAmount, Integer getAmount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyAmount = buyAmount;
        this.getAmount = getAmount;
        this.startDate = startDate;
        this.endDate = endDate;
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
