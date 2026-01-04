package ci553.happyshop.client.picker;

import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Adapter for PickerView to embed it inside a Tab without modifying the view.
 */
public class PickerTabView {

    private final PickerView view;
    private final PickerController controller;
    private final PickerModel model;

    private Parent cached;

    public PickerTabView() {
        this.view = new PickerView();
        this.controller = new PickerController();
        this.model = new PickerModel();

        // ✅ CORRECT MVC WIRING
        view.controller = controller;
        view.model = model;

        controller.pickerModel = model;
    }

    public Parent getContent() {
        if (cached != null) return cached;

        Stage temp = new Stage();
        view.start(temp);
        cached = temp.getScene().getRoot();
        temp.close();

        return cached;
    }
}