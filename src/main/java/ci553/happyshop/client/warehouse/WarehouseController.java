package ci553.happyshop.client.warehouse;

import java.io.IOException;
import java.sql.SQLException;

public class WarehouseController {

    public WarehouseModel model;

    public void process(String action) {

        try {
            switch (action) {

                case "Search":
                case "🔍":
                    model.doSearch();
                    break;

                case "Edit":
                    model.doEdit();
                    break;

                case "Delete":
                    model.doDelete();
                    break;

                case "Submit":
                    model.doSummit();
                    break;

                case "Cancel":
                    model.doCancel();
                    break;

                case "+":
                    model.doChangeStockBy("add");
                    break;

                case "-":
                    model.doChangeStockBy("sub");
                    break;

                default:
                    System.out.println("Unknown action: " + action);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}