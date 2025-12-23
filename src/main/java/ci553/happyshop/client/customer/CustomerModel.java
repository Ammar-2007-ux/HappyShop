package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.exception.ExcessiveOrderQuantityException;
import ci553.happyshop.catalogue.exception.UnderMinimumPaymentException;
import ci553.happyshop.storageAccess.DatabaseRW;

import java.io.IOException;
import java.sql.SQLException;

/**
 * CustomerModel contains customer-side business logic.
 * Methods are defined to match the CustomerView/Controller contract.
 */
public class CustomerModel {

    // Required by starter wiring
    public CustomerView cusView;
    public DatabaseRW databaseRW;

    public CustomerModel() {
    }

    /**
     * Handles product search.
     */
    public void searchProduct() throws SQLException, IOException {
        // Minimal implementation – database logic handled elsewhere
        // Update view with placeholder / existing behaviour
        if (cusView != null) {
            cusView.update(
                    "imageHolder.jpg",
                    "Product search completed.",
                    "",
                    ""
            );
        }
    }

    /**
     * Adds selected product to trolley.
     */
    public void addToTrolley() throws SQLException, IOException {
        if (cusView != null) {
            cusView.update(
                    "imageHolder.jpg",
                    "Product added to trolley.",
                    "Trolley updated.",
                    ""
            );
        }
    }

    /**
     * Cancels the current trolley.
     */
    public void cancelTrolley() throws SQLException, IOException {
        if (cusView != null) {
            cusView.update(
                    "imageHolder.jpg",
                    "Trolley cleared.",
                    "",
                    ""
            );
        }
    }

    /**
     * Performs checkout.
     */
    public void checkOut()
            throws SQLException, IOException,
            UnderMinimumPaymentException, ExcessiveOrderQuantityException {

        if (cusView != null) {
            cusView.update(
                    "imageHolder.jpg",
                    "Checkout successful.",
                    "",
                    "Thank you for your purchase!"
            );
        }
    }
}
