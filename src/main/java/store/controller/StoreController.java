package store.controller;

import static store.utils.constant.ProductConstant.PRODUCT_INPUT_LENGTH;
import static store.utils.constant.ProductConstant.PRODUCT_SPILT_REGEX;
import static store.utils.constant.ProductConstant.QUANTITY_INDEX;
import static store.utils.message.ErrorMessage.INVALID_INPUT;

import java.util.List;
import java.util.Map;
import store.converter.ProductConverter;
import store.converter.PromotionConverter;
import store.model.Inventory;
import store.model.Product;
import store.model.Promotion;
import store.view.InputView;

public class StoreController {

    public void openStore(){
        List<String> inputPromotions = InputView.inputData("src/main/resources/promotions.md");
        List<String> inputProducts = InputView.inputData("src/main/resources/products.md");

        List<Promotion> promotions = PromotionConverter.convertToPromotion(inputPromotions);
        Map<Product, Integer> products = ProductConverter.convertToProduct(inputProducts, promotions);

    }

}
