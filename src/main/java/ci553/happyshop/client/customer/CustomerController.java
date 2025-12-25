package ci553.happyshop.client.customer;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for customer actions.
 * Cleaned up to match current CustomerModel behaviour.
 */
public class CustomerController {

    // Required by starter wiring
    public CustomerModel cusModel;

    public CustomerController() {
    }

    public CustomerController(CustomerModel model) {
        this.cusModel = model;
    }

    /**
     * Handles actions triggered by CustomerView buttons.
     */
    public void doAction(String action) throws SQLException, IOException {

        if (cusModel == null) return;

        switch (action) {

            case "Search":
                cusModel.searchProduct();
                break;

            case "Add to Trolley":
                cusModel.addToTrolley();
                break;

            case "Cancel":
                cusModel.cancelTrolley();
                break;

            case "Check Out":
                cusModel.checkOut();
                break;

            case "OK & Close":
                // No model action needed
                break;

            default:
                break;
        }
    }
}
