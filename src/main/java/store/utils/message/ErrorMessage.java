package store.utils.message;

public class ErrorMessage {


    public static final String PROMOTION_BUY_AMOUNT_GREATER_THAN_ZERO = "[ERROR] 프로모션의 구매 수량은 항상 0보다 커야 합니다.";
    public static final String PROMOTION_GET_AMOUNT_GREATER_THAN_ZERO = "[ERROR] 프로모션의 증정 수량은 항상 0보다 커야 합니다.";
    public static final String INVALID_PROMOTION_DATE = "[ERROR] 프로모션의 시작 날짜는 종료 날짜 이전이어야 한다.";

    public static final String NOT_ENOUGH_STOCK = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    public static final String ORDER_QUANTITY_GREATER_THAN_ZERO = "[ERROR] 주문 수량은 항상 0보다 커야 합니다.";

    public static final String NON_EXIST_PRODUCT = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";

    public static final String INVALID_PROMOTION_NAME = "[ERROR] 유효하지 않은 프로모션 이름입니다.";
    public static final String INVALID_INPUT = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";

    public static final String STOP_SHOPPING = "[ERROR] 주문을 종료하셨습니다.";
}
