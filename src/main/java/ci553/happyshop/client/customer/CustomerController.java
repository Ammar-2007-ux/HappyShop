package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.exception.ExcessiveOrderQuantityException;
import ci553.happyshop.catalogue.exception.UnderMinimumPaymentException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for customer actions.
 * Catches business exceptions so the View does not need to.
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

        try {
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
        catch (UnderMinimumPaymentException e) {
            cusModel.cusView.update(
                    "imageHolder.jpg",
                    "Minimum order value is £5. Please add more items.",
                    "",
                    ""
            );
        }
        catch (ExcessiveOrderQuantityException e) {
            cusModel.cusView.update(
                    "imageHolder.jpg",
                    "Some items exceeded the maximum quantity allowed.",
                    "",
                    ""
            );
        }
    }
}
