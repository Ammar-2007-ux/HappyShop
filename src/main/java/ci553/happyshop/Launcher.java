package ci553.happyshop;

import ci553.happyshop.client.warehouse.WarehouseClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Launch ONLY the Warehouse window
        new WarehouseClient().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
