package store.converter;

import static store.utils.constant.OrderConstant.ORDER_LIST_SPILT_REGEX;
import static store.utils.constant.OrderConstant.ORDER_SPILT_REGEX;

import java.util.ArrayList;
import java.util.List;
import store.model.Order;
import store.utils.message.ErrorMessage;

public class OrderConverter {

    public static List<Order> convertToOrder(String input) {
        validateInput(input);
        List<Order> orders = new ArrayList<>();
        String[] inputOrders = splitOrders(input);

        for (String order : inputOrders) {
            Order parsedOrder = parseOrder(order);
            orders.add(parsedOrder);
        }

        return orders;
    }

    // 문자열로부터 주문들을 구분하여 반환
    private static String[] splitOrders(String input) {
        return input.split(ORDER_LIST_SPILT_REGEX);
    }

    // 주문 문자열을 Order 객체로 변환
    private static Order parseOrder(String order) {
        String[] split = extractProductAndQuantity(order);

        if (split.length != 2) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_ORDER_INPUT);
        }

        String productName = split[0];
        Integer quantity = parseQuantity(split[1]);

        return new Order(productName, quantity);
    }

    // 문자열에서 제품 이름과 수량을 분리
    private static String[] extractProductAndQuantity(String order) {
        String substring = order.substring(1, order.length() - 1);  // 양 끝 괄호 제거
        return substring.split(ORDER_SPILT_REGEX);
    }

    // 수량을 정수로 파싱
    private static Integer parseQuantity(String quantityString) {
        try {
            return Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_ORDER_INPUT);
        }
    }

    private static void validateInput(String input) {
        // 쉼표로 구분된 주문 항목들 분리
        String[] items = splitOrders(input);

        for (String item : items) {
            // 각 항목이 대괄호로 둘러싸여 있는지 확인
            if (!item.startsWith("[") || !item.endsWith("]")) {
                throw new IllegalArgumentException(ErrorMessage.INVALID_ORDER_INPUT);
            }

            // 대괄호를 제거하고 상품명과 수량 분리
            String[] parts = extractProductAndQuantity(item);

            // 상품명과 수량이 정확히 분리되었는지 확인
            if (parts.length != 2 || parts[0].isEmpty() || !isNumeric(parts[1])) {
                throw new IllegalArgumentException(ErrorMessage.INVALID_ORDER_INPUT);
            }
        }
    }

    // 숫자 여부 확인
    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
