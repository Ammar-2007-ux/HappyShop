package ci553.happyshop.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WarehouseNotificationHub {

    private static final List<Consumer<String>> listeners = new ArrayList<>();

    public static void register(Consumer<String> listener) {
        listeners.add(listener);
    }

    public static void notifyWarehouse(String message) {
        for (Consumer<String> listener : listeners) {
            listener.accept(message);
        }
    }
}