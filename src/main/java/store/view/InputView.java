package store.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputView {

    public static List<String> inputData(String filePath){

        try {
            List<String> inputPromotions = new ArrayList<>();
            readData(inputPromotions, filePath);
            return inputPromotions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readData(List<String> inputPromotions, String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String input;
        while ((input = bufferedReader.readLine()) != null){
            inputPromotions.add(input);
        }
    }

}
