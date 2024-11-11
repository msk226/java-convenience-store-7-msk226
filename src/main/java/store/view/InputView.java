package store.view;


import camp.nextstep.edu.missionutils.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputView {

    public static String input(String message) {
        while (true){
            try {
                System.out.println(message);
                return Console.readLine();
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }

    }

    public static List<String> inputData(String filePath) {

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
        while ((input = bufferedReader.readLine()) != null) {
            inputPromotions.add(input);
        }
    }

}
