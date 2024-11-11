package store.converter;

import static store.utils.constant.ProductConstant.NAME_INDEX;
import static store.utils.constant.ProductConstant.PRICE_INDEX;
import static store.utils.constant.ProductConstant.PRODUCT_INPUT_LENGTH;
import static store.utils.constant.ProductConstant.PRODUCT_SPILT_REGEX;
import static store.utils.constant.ProductConstant.PROMOTION_INDEX;
import static store.utils.constant.ProductConstant.QUANTITY_INDEX;
import static store.utils.constant.PromotionConstant.PROMOTION_TITLE_INDEX;
import static store.utils.message.ErrorMessage.INVALID_INPUT;
import static store.utils.message.ErrorMessage.INVALID_PROMOTION_NAME;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.model.Product;
import store.model.Promotion;
import store.utils.message.ErrorMessage;

public class ProductConverter {

    public static Map<Product, Integer> convertToProduct(List<String> inputProducts, List<Promotion> promotions) {
        Map<Product, Integer> products = new LinkedHashMap<>();
        for (String inputProduct : inputProducts.subList(PROMOTION_TITLE_INDEX, inputProducts.size())) {
            String[] input = parseInputProduct(inputProduct);
            Product product = createProduct(input, promotions);
            products.put(product, getQuantity(input));
        }
        return products;
    }

    private static int getQuantity(String[] input) {
        return Integer.parseInt(input[QUANTITY_INDEX]);
    }

    private static String[] parseInputProduct(String inputProduct) {
        String[] input = inputProduct.split(PRODUCT_SPILT_REGEX);
        if (input.length != PRODUCT_INPUT_LENGTH) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        return input;
    }

    private static Product createProduct(String[] input, List<Promotion> promotions) {
        validateProductInput(input);
        String name = getProductName(input);
        Integer price = parsePrice(input);
        Promotion promotion = parsePromotion(input, promotions);

        return new Product(name, price, promotion);
    }

    private static void validateProductInput(String[] input) {
        if (input.length != PRODUCT_INPUT_LENGTH) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private static String getProductName(String[] input) {
        return input[NAME_INDEX];
    }

    private static Integer parsePrice(String[] input) {
        try {
            return Integer.valueOf(input[PRICE_INDEX]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private static Promotion parsePromotion(String[] input, List<Promotion> promotions) {
        String promotionName = input[PROMOTION_INDEX];
        return findPromotion(promotionName, promotions);
    }

    private static Promotion findPromotion(String promotionName, List<Promotion> promotions) {
        for (Promotion promotion : promotions) {
            if (promotionName.equals(promotion.getName())) {
                return promotion;
            }
        }
        return null;
    }

}
