package ci553.happyshop.client.customer;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class CustomerClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) {

        CustomerView cusView = new CustomerView();
        CustomerModel cusModel = new CustomerModel();
        CustomerController cusController = new CustomerController(cusModel);

        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        cusView.cusController = cusController;
        cusController.cusModel = cusModel;
        cusModel.cusView = cusView;
        cusModel.databaseRW = databaseRW;

        cusView.start(window);
    }
}
