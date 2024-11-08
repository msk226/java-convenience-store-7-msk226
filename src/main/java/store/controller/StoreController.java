package store.controller;

import java.util.List;
import store.view.InputView;

public class StoreController {

    public void openStore(){
        List<String> inputPromotions = InputView.inputData("src/main/resources/promotions.md");
        List<String> inputProducts = InputView.inputData("src/main/resources/products.md");



    }
}
