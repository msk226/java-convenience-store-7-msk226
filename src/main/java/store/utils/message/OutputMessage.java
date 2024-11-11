package store.utils.message;

public class OutputMessage {

    // 각 출력에 대한 상수들
    public static final String PREFIX = "- ";
    public static final String BLANK = " ";
    public static final String TAB = " ";
    public static final String COUNT = "개";
    public static final String WON = "원";
    public static final String EMPTY = "재고 없음";
    public static final String DEFAULT_PROMOTION_NAME = "";
    public static final String COUNT_FORMAT = "#,###";

    // W편의점 환영 메시지
    public static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";

    // 영수증 관련 메시지
    public static final String RECEIPT_WELCOME_MESSAGE = "==============W 편의점================";

    public static final String RECEIPT_TITLE_MESSAGE = String.format("%-15s%-15s%-5s", "상품명", "수량", "금액");
    public static final String PRODUCT_MESSAGE = "%-15s%-15d%,-5d\n";  // 상품명 18자리, 수량 15자리, 금액 15자리

    // 프로모션 관련 메시지
    public static final String PROMOTION = "%-15s%-15d\n";

    // 금액 관련 메시지
    public static final String TOTAL_AMOUNT_LABEL = "총구매액";
    public static final String DISCOUNT_AMOUNT_LABEL = "행사할인";
    public static final String MEMBERSHIP_AMOUNT_LABEL = "멤버십할인";
    public static final String PAY_AMOUNT_LABEL = "내실돈";

    // 금액 출력 형식
    public static final String TOTAL_AMOUNT = "%-15s%20s\n";
    public static final String DISCOUNT_AMOUNT = "%-15s%20s\n";
    public static final String MEMBERSHIP_AMOUNT = "%-15s%20s\n";

    public static final String RESULT_ZERO = "-0";
    public static final String STRING_FORMAT = "-%s";
    public static final String INTEGER_FORMAT = "%,d";


    public static final String PAY_AMOUNT = "%-15s%20s\n";

    // 구분선
    public static final String PROMOTION_DIVISION = "=============증   정===============";
    public static final String DIVISION = "====================================";
}
