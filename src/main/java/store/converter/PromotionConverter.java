package store.converter;

import static store.utils.constant.PromotionConstant.BUY_AMOUNT_INDEX;
import static store.utils.constant.PromotionConstant.END_DATE_INDEX;
import static store.utils.constant.PromotionConstant.GET_AMOUNT_INDEX;
import static store.utils.constant.PromotionConstant.NAME_INDEX;
import static store.utils.constant.PromotionConstant.PROMOTION_DATE_FORMAT;
import static store.utils.constant.PromotionConstant.PROMOTION_INPUT_LENGTH;
import static store.utils.constant.PromotionConstant.PROMOTION_SPILT_REGEX;
import static store.utils.constant.PromotionConstant.PROMOTION_TITLE_INDEX;
import static store.utils.constant.PromotionConstant.START_DATE_INDEX;
import static store.utils.message.ErrorMessage.*;
import static store.utils.message.ErrorMessage.INVALID_INPUT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import store.model.Promotion;
import store.utils.message.ErrorMessage;

public class PromotionConverter {

    public static List<Promotion> convertToPromotion(List<String> inputPromotions){
        List<Promotion> promotions = new ArrayList<>();

        for (String inputPromotion : inputPromotions.subList(PROMOTION_TITLE_INDEX, inputPromotions.size())){
            String[] input = parseInputPromotion(inputPromotion);
            Promotion promotion = createPromotion(input);
            promotions.add(promotion);
        }
        return promotions;
    }

    private static Promotion createPromotion(String[] input) {
        String name = parseName(input);
        Integer buyAmount = parseBuyAmount(input);
        Integer getAmount = parseGetAmount(input);
        LocalDate startDate = parseStartDate(input);
        LocalDate endDate = parseEndDate(input);
        return new Promotion(name, buyAmount, getAmount, startDate, endDate);
    }

    private static String parseName(String[] input) {
        return input[NAME_INDEX];
    }

    private static Integer parseBuyAmount(String[] input) {
        try {
            return Integer.valueOf(input[BUY_AMOUNT_INDEX]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid buy amount");
        }
    }

    private static Integer parseGetAmount(String[] input) {
        try {
            return Integer.valueOf(input[GET_AMOUNT_INDEX]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid get amount");
        }
    }

    private static LocalDate parseStartDate(String[] input) {
        try {
            return LocalDate.parse(input[START_DATE_INDEX], DateTimeFormatter.ofPattern(PROMOTION_DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid start date format");
        }
    }

    private static LocalDate parseEndDate(String[] input) {
        try {
            return LocalDate.parse(input[END_DATE_INDEX], DateTimeFormatter.ofPattern(PROMOTION_DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid end date format");
        }
    }


    private static String[] parseInputPromotion(String inputPromotion) {
        String[] input = inputPromotion.split(PROMOTION_SPILT_REGEX);
        if (input.length != PROMOTION_INPUT_LENGTH) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        return input;
    }
}
