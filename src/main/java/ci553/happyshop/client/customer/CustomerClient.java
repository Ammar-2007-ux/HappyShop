package ci553.happyshop.client.customer;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * A standalone Customer Client that can be run independently without launching the full system.
 * Designed for early-stage testing, though full functionality may require other clients to be active.
 */
public class CustomerClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates the Model, View, and Controller objects and links them together.
     */
    @Override
    public void start(Stage window) {

        CustomerView cusView = new CustomerView();
        CustomerModel cusModel = new CustomerModel();
        CustomerController cusController = new CustomerController(cusModel);

        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        // Wire MVC components together (as expected by the starter code)
        cusView.cusController = cusController;
        cusController.cusModel = cusModel;
        cusModel.cusView = cusView;
        cusModel.databaseRW = databaseRW;

        // Start the customer UI
        cusView.start(window);
    }
}
