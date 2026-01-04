package ci553.happyshop.client.orderTracker;

import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.orderManagement.OrderState;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class OrderTracker {

    private final TextArea taDisplay = new TextArea();

    public OrderTracker() {

        Label title = new Label("📦 Orders Being Prepared");
        title.setStyle(UIStyle.labelTitleStyle);

        taDisplay.setEditable(false);
        taDisplay.setStyle(UIStyle.textFiledStyle);

        VBox root = new VBox(10, title, taDisplay);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(UIStyle.rootStyleGray);

        Scene scene = new Scene(root, UIStyle.trackerWinWidth, UIStyle.trackerWinHeight);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Order Tracker");

        WinPosManager.registerWindow(stage, UIStyle.trackerWinWidth, UIStyle.trackerWinHeight);
        stage.show();

        // 🔥 THIS WAS MISSING 🔥
        OrderHub.getOrderHub().registerOrderTracker(this);
    }

    // CALLED BY OrderHub
    public void setOrderMap(TreeMap<Integer, OrderState> orderMap) {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, OrderState> entry : orderMap.entrySet()) {
            int orderId = entry.getKey();
            OrderState state = entry.getValue();

            sb.append("Order ").append(orderId)
                    .append(" – ").append(state).append("\n");

            sb.append(readOrderContents(orderId));
            sb.append("\n--------------------------\n");
        }

        taDisplay.setText(sb.toString());
    }

    private String readOrderContents(int orderId) {
        try {
            Path p1 = StorageLocation.progressingPath.resolve(orderId + ".txt");
            Path p2 = StorageLocation.orderedPath.resolve(orderId + ".txt");

            if (Files.exists(p1)) return Files.readString(p1);
            if (Files.exists(p2)) return Files.readString(p2);
        } catch (IOException ignored) {}

        return "No order details found\n";
    }
}