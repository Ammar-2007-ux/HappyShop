package ci553.happyshop.client;

import ci553.happyshop.client.customer.CustomerClient;
import javafx.application.Application;

/**
 * Launches the CustomerClient directly.
 * No warehouse or picker UI is exposed.
 */
public class UnifiedMain {

    public static void main(String[] args) {
        Application.launch(CustomerClient.class, args);
    }
}
