package ci553.happyshop.client.warehouse;

public class WarehouseController {

    public WarehouseModel model;

    public WarehouseController(WarehouseModel model) {
        this.model = model;
    }

    public WarehouseController() {}

    public void process(String action) {
        try {
            switch (action) {

                case "Search", "🔍" -> model.doSearch();
                case "Edit" -> model.doEdit();
                case "Delete" -> model.doDelete();
                case "Submit" -> model.doSummit();
                case "Cancel" -> model.doCancel();
                case "+" -> model.doChangeStockBy("add");
                case "-" -> model.doChangeStockBy("sub");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}