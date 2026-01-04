package ci553.happyshop.client.picker;

import ci553.happyshop.orderManagement.OrderState;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

/**
 * PickerView displays orders that need preparing and allows the picker
 * to mark them as progressing or collected.
 */
public class PickerView {

    public PickerController controller;
    public PickerModel model;

    private ListView<Integer> lvOrders;
    private TextArea taOrderDetail;

    private final int WIDTH = UIStyle.pickerWinWidth;
    private final int HEIGHT = UIStyle.pickerWinHeight;

    public void start(Stage window) {

        Label laTitle = new Label("Orders to Prepare");
        laTitle.setStyle(UIStyle.labelTitleStyle);

        lvOrders = new ListView<>();
        lvOrders.setPrefHeight(200);

        lvOrders.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                model.selectOrder(newVal);
                taOrderDetail.setText(model.displayTaOrderDetail);
            }
        });

        taOrderDetail = new TextArea();
        taOrderDetail.setEditable(false);
        taOrderDetail.setStyle(UIStyle.textFiledStyle);

        Button btnPrepare = new Button("Start Preparing");
        btnPrepare.setStyle(UIStyle.buttonStyle);
        btnPrepare.setOnAction(e -> {
            int orderId = model.getSelectedOrderId();
            if (orderId != -1) {
                controller.doProgressing(orderId);
            }
        });

        Button btnCollected = new Button("Mark Collected");
        btnCollected.setStyle(UIStyle.buttonStyle);
        btnCollected.setOnAction(e -> {
            int orderId = model.getSelectedOrderId();
            if (orderId != -1) {
                controller.doCollected(orderId);
            }
        });

        HBox hbBtns = new HBox(10, btnPrepare, btnCollected);
        hbBtns.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, laTitle, lvOrders, taOrderDetail, hbBtns);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(UIStyle.rootStyleGray);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        window.setScene(scene);
        window.setTitle("📦 Order Picker");

        WinPosManager.registerWindow(window, WIDTH, HEIGHT);
        window.show();
    }

    /**
     * Called by PickerModel when order map updates
     */
    public void refreshOrderList(Map<Integer, OrderState> orderMap) {
        lvOrders.getItems().clear();
        for (Map.Entry<Integer, OrderState> entry : orderMap.entrySet()) {
            if (entry.getValue() != OrderState.Collected) {
                lvOrders.getItems().add(entry.getKey());
            }
        }
    }
}