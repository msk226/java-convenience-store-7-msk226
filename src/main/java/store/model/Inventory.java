package store.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import store.utils.message.ErrorMessage;

public class Inventory {
    private final Map<Product, Integer> stock = new HashMap<>();

    public boolean existsByProduct(Product product){
        return stock.containsKey(product);
    }

    public Set<Product> getProducts(){
        return stock.keySet();
    }
    public void addStock(Product product, Integer quantity){
        stock.put(product, stock.getOrDefault(product, 0) + quantity);
    }
    public int getStock(Product product) {
        return stock.getOrDefault(product, 0);
    }

    public void reduceStock(Product product, int quantity) {
        int currentStock = getCurrentStock(product, quantity);
        stock.put(product, currentStock - quantity);
    }

    private int getCurrentStock(Product product, int quantity) {
        int currentStock = stock.getOrDefault(product, 0);
        if (currentStock < quantity) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ENOUGH_STOCK);
        }
        return currentStock;
    }
}
