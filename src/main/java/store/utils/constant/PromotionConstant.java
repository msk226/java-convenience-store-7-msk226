package store.utils.constant;

import java.time.LocalDate;

public class PromotionConstant {


    public static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    public static final Integer PROMOTION_TITLE_INDEX = 1;

    public static final Integer NAME_INDEX = 0;
    public static final Integer BUY_AMOUNT_INDEX = 1;
    public static final Integer GET_AMOUNT_INDEX = 2;
    public static final Integer START_DATE_INDEX = 3;
    public static final Integer END_DATE_INDEX = 4;

    public static final Integer PROMOTION_INPUT_LENGTH = 5;

    public static final String PROMOTION_SPILT_REGEX = ",";
    public static final String PROMOTION_DATE_FORMAT = "yyyy-MM-dd";


    public static final String DEFAULT_NAME = "null";
    public static final Integer DEFAULT_BUY_AMOUNT = 0;
    public static final Integer DEFAULT_GET_AMOUNT = 0;
    public static final LocalDate DEFAULT_START_DATE = LocalDate.MIN;
    public static final LocalDate DEFAULT_END_DATE = LocalDate.MAX;


}
