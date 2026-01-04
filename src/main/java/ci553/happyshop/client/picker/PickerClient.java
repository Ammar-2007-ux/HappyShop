package ci553.happyshop.client.picker;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Standalone Picker client
 */
public class PickerClient extends Application {

    @Override
    public void start(Stage stage) {

        PickerView view = new PickerView();
        PickerModel model = new PickerModel();
        PickerController controller = new PickerController();

        // ✅ CORRECT WIRING (IMPORTANT)
        view.controller = controller;
        view.model = model;

        controller.pickerModel = model;

        view.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}