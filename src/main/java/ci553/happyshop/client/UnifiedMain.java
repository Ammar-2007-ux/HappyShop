package ci553.happyshop.client;

import ci553.happyshop.client.customer.CustomerTabView;
import ci553.happyshop.client.picker.PickerTabView;
import ci553.happyshop.client.warehouse.WarehouseTabView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Optional single-window launcher that shows existing views as tabs.
 * Does not replace Main —
 */
public class UnifiedMain extends Application {

    @Override
    public void start(Stage stage) {
        // Adapters wrap the existing views without changing them
        CustomerTabView customerTab = new CustomerTabView();
        PickerTabView pickerTab = new PickerTabView();
        WarehouseTabView warehouseTab = new WarehouseTabView();

        TabPane tabs = new TabPane(
                makeTab("Customer", customerTab.getContent()),
                makeTab("Picker", pickerTab.getContent()),
                makeTab("Warehouse", warehouseTab.getContent())
        );

        stage.setTitle("HappyShop — Unified");
        stage.setScene(new Scene(tabs, 1100, 720));
        stage.show();
    }

    private Tab makeTab(String title, javafx.scene.Parent content) {
        Tab t = new Tab(title, content);
        t.setClosable(false);
        return t;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
