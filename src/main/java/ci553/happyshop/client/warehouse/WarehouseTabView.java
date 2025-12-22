package ci553.happyshop.client.warehouse;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Adapter for WarehouseView to embed it inside a Tab without changing the view.
 */
public class WarehouseTabView {

    private final WarehouseView view;
    private final WarehouseController controller;
    private final WarehouseModel model;
    private final DatabaseRW databaseRW;

    private Parent cached;

    public WarehouseTabView() {
        this.view = new WarehouseView();
        this.controller = new WarehouseController();
        this.model = new WarehouseModel();
        this.databaseRW = DatabaseRWFactory.createDatabaseRW();

        view.controller = controller;
        controller.model = model;
        model.view = view;
        model.databaseRW = databaseRW;
    }

    public Parent getContent() {
        if (cached != null) return cached;

        Stage temp = new Stage();
        view.start(temp); // builds the scene with the existing view logic

        // After start, wire the dependent windows exactly like Main
        HistoryWindow historyWindow = new HistoryWindow();
        AlertSimulator alertSimulator = new AlertSimulator();
        model.historyWindow = historyWindow;
        model.alertSimulator = alertSimulator;
        historyWindow.warehouseView = view;
        alertSimulator.warehouseView = view;

        cached = temp.getScene().getRoot();
        temp.close();
        return cached;
    }
}
