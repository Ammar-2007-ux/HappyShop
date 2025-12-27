package ci553.happyshop.client;

import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.orderManagement.OrderHub;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Initialise orders
        OrderHub.getOrderHub().initializeOrderMap();

        // Create Order Tracker window
        new OrderTracker();   // <-- THIS opens the window
    }

    public static void main(String[] args) {
        launch(args);
    }
}
