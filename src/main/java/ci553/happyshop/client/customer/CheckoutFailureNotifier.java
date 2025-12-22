package ci553.happyshop.client.customer;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WindowBounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A lightweight non-blocking popup used to show checkout validation failures
 * (e.g., under-minimum payment, excessive per-item quantity).
 *
 * This mirrors the pattern used by {@link RemoveProductNotifier} so the UI behaves
 * consistently across customer flows.
 */
public class CheckoutFailureNotifier {

    public CustomerView cusView; // used to position the window near the main customer window

    private static final int WIDTH = UIStyle.removeProNotifierWinWidth;
    private static final int HEIGHT = 180;

    private Stage window;
    private Scene scene;
    private TextArea taMsg;

    private void createScene() {
        Label laTitle = new Label("\u26A0 Checkout could not be completed."); // ⚠️
        laTitle.setStyle(UIStyle.alertTitleLabelStyle);

        taMsg = new TextArea();
        taMsg.setEditable(false);
        taMsg.setWrapText(true);
        taMsg.setPrefHeight(70);
        taMsg.setStyle(UIStyle.alertContentTextAreaStyle);

        Button btnOk = new Button("Ok");
        btnOk.setStyle(UIStyle.alertBtnStyle);
        btnOk.setOnAction(e -> window.close());

        HBox hb = new HBox(10, btnOk);
        hb.setAlignment(Pos.CENTER_RIGHT);

        GridPane pane = new GridPane();
        pane.setHgap(5);
        pane.setVgap(5);
        pane.add(laTitle, 0, 0);
        pane.add(taMsg, 0, 1);
        pane.add(hb, 0, 2);
        pane.setStyle(UIStyle.rootStyleGray);

        scene = new Scene(pane, WIDTH, HEIGHT);
    }

    private void createWindow() {
        if (scene == null) {
            createScene();
        }

        window = new Stage();
        window.initModality(Modality.NONE);
        window.setTitle("Checkout message");
        window.setScene(scene);

        if (cusView != null) {
            WindowBounds bounds = cusView.getWindowBounds();
            window.setX(bounds.x + bounds.width - WIDTH - 10);
            window.setY(bounds.y + bounds.height / 2 + 40);
        }
        window.show();
    }

    public void showMsg(String msg) {
        if (window == null || !window.isShowing()) {
            createWindow();
        }
        taMsg.setText(msg);
        window.toFront();
    }

    public void closeNotifierWindow() {
        if (window != null && window.isShowing()) {
            window.close();
        }
    }
}
