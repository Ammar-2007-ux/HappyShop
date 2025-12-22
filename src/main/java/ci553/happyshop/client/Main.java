package ci553.happyshop.client;

import ci553.happyshop.client.customer.*;
import ci553.happyshop.client.emergency.EmergencyExit;
import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.client.picker.PickerController;
import ci553.happyshop.client.picker.PickerModel;
import ci553.happyshop.client.picker.PickerView;
import ci553.happyshop.client.warehouse.*;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.utility.WindowLayout;   // <-- ADDED

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;                     // <-- ADDED
import java.util.List;                         // <-- ADDED

public class Main extends Application {

    private final List<Stage> openStages = new ArrayList<>();   // <-- ADDED

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws IOException {
        startCustomerClient();
        startPickerClient();
        startOrderTracker();

        startCustomerClient();
        startPickerClient();
        startOrderTracker();

        initializeOrderMap();

        startWarehouseClient();
        startWarehouseClient();

        startEmergencyExit();

        // <-- ADDED (Tile all created windows neatly in 2 rows × 3 columns)
        WindowLayout.tileGrid(openStages, 2, 3, 12);
    }

    private void startCustomerClient(){
        CustomerView cusView = new CustomerView();
        CustomerController cusController = new CustomerController();
        CustomerModel cusModel = new CustomerModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        cusView.cusController = cusController;
        cusController.cusModel = cusModel;
        cusModel.cusView = cusView;
        cusModel.databaseRW = databaseRW;

        Stage s = new Stage();     // <-- ADDED
        cusView.start(s);          // (changed from new Stage())
        openStages.add(s);         // <-- ADDED
    }

    private void startPickerClient(){
        PickerModel pickerModel = new PickerModel();
        PickerView pickerView = new PickerView();
        PickerController pickerController = new PickerController();

        pickerView.pickerController = pickerController;
        pickerController.pickerModel = pickerModel;
        pickerModel.pickerView = pickerView;
        pickerModel.registerWithOrderHub();

        Stage s = new Stage();     // <-- ADDED
        pickerView.start(s);       // (changed from new Stage())
        openStages.add(s);         // <-- ADDED
    }

    private void startOrderTracker(){
        OrderTracker orderTracker = new OrderTracker();
        orderTracker.registerWithOrderHub();
    }

    private void initializeOrderMap(){
        OrderHub orderHub = OrderHub.getOrderHub();
        orderHub.initializeOrderMap();
    }

    private void startWarehouseClient(){
        WarehouseView view = new WarehouseView();
        WarehouseController controller = new WarehouseController();
        WarehouseModel model = new WarehouseModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        view.controller = controller;
        controller.model = model;
        model.view = view;
        model.databaseRW = databaseRW;

        Stage s = new Stage();     // <-- ADDED
        view.start(s);             // (changed from new Stage())
        openStages.add(s);         // <-- ADDED

        HistoryWindow historyWindow = new HistoryWindow();
        AlertSimulator alertSimulator = new AlertSimulator();

        model.historyWindow = historyWindow;
        model.alertSimulator = alertSimulator;
        historyWindow.warehouseView = view;
        alertSimulator.warehouseView = view;
    }

    private void startEmergencyExit(){
        EmergencyExit.getEmergencyExit();
    }
}