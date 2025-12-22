package ci553.happyshop.client.customer;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Adapter that builds the existing CustomerView and exposes it as a Node
 * so it can be embedded inside a Tab.
 */
public class CustomerTabView {

    private final CustomerView view;
    private final CustomerController controller;
    private final CustomerModel model;
    private final DatabaseRW databaseRW;

    private Parent cached; // reuse once built

    public CustomerTabView() {
        this.view = new CustomerView();
        this.controller = new CustomerController();
        this.model = new CustomerModel();
        this.databaseRW = DatabaseRWFactory.createDatabaseRW();

        // Wire exactly like Main does
        view.cusController = controller;
        controller.cusModel = model;
        model.cusView = view;
        model.databaseRW = databaseRW;
    }

    //Builds the UI using a temporary Stage and returns the root node.
    public Parent getContent() {
        if (cached != null) return cached;
        Stage temp = new Stage();
        view.start(temp);                 // reuse existing startup code
        cached = temp.getScene().getRoot();
        temp.close();
        return cached;
    }
}
