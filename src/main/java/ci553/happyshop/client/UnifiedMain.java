package ci553.happyshop.client;

import ci553.happyshop.client.customer.CustomerClient;
import ci553.happyshop.orderManagement.OrderHub;
import javafx.application.Application;
import javafx.stage.Stage;

public class UnifiedMain extends Application {

    @Override
    public void start(Stage primaryStage) {

        // IMPORTANT: load existing orders & prepare OrderHub
        OrderHub.getOrderHub().initializeOrderMap();

        // Start customer UI (orders are created from here)
        try {
            new CustomerClient().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}