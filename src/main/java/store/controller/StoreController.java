package store.controller;


import java.util.List;
import java.util.Map;
import store.converter.ProductConverter;
import store.converter.PromotionConverter;
import store.model.Inventory;
import store.model.Product;
import store.model.Promotion;
import store.model.Store;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    public void openStore(){
        OutputView.printWelcomeMessage();

        List<String> inputPromotions = InputView.inputData("src/main/resources/promotions.md");
        List<String> inputProducts = InputView.inputData("src/main/resources/products.md");

        List<Promotion> promotions = PromotionConverter.convertToPromotion(inputPromotions);
        Map<Product, Integer> products = ProductConverter.convertToProduct(inputProducts, promotions);
//
        storeService.initializeStore(products);

        OutputView.printProducts(products);
    }

}
