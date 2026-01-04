package ci553.happyshop.client;

import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.orderManagement.OrderHub;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Keep JavaFX alive
        primaryStage.setTitle("Order Tracker Launcher");
        primaryStage.show();

        // 🔴 CREATE TRACKER FIRST
        new OrderTracker();

        // 🔴 THEN load orders (this notifies tracker)
        OrderHub.getOrderHub().initializeOrderMap();
    }

    public static void main(String[] args) {
        launch(args);
    }
}