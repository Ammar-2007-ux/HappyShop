package ci553.happyshop.client.customer;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerController {

    public CustomerModel cusModel;

    // no-arg constructor
    public CustomerController() {
    }

    // constructor with model
    public CustomerController(CustomerModel model) {
        this.cusModel = model;
    }

    // 🔧 now DECLARES IOException so CustomerView's try/catch is valid
    public void doAction(String action) throws SQLException, IOException {

        switch (action) {
            case "Search":
                cusModel.searchProduct();
                break;

            case "Add to Trolley":
                cusModel.addToTrolley();
                break;

            case "Check Out":
                cusModel.checkOut();
                break;

            case "Cancel":
                break;
        }
    }
}
