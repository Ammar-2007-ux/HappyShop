package ci553.happyshop;

import ci553.happyshop.client.customer.CustomerClient;
import javafx.application.Application;

public class Launcher {

    public static void main(String[] args) {
        Application.launch(CustomerClient.class, args);
    }
}
