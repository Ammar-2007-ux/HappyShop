package ci553.happyshop.client.warehouse;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Standalone Warehouse client.
 * Wires View, Controller, and Model correctly.
 */
public class WarehouseClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) {

        // Create MVC components
        WarehouseView view = new WarehouseView();
        WarehouseController controller = new WarehouseController();
        WarehouseModel model = new WarehouseModel();

        // Create database access
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        //  WIRE MVC PROPERLY
        view.controller = controller;
        controller.model = model;
        model.view = view;
        model.databaseRW = databaseRW;

        // Start UI
        view.start(window);
    }
}