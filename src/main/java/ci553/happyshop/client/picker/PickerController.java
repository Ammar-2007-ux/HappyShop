package ci553.happyshop.client.picker;

/**
 * PickerController forwards UI actions to PickerModel.
 */
public class PickerController {

    public PickerModel pickerModel;

    /**
     * Called when picker clicks "Start Preparing"
     */
    public void doProgressing(int orderId) {
        pickerModel.startPreparing(orderId);
    }

    /**
     * Called when picker clicks "Mark Collected"
     */
    public void doCollected(int orderId) {
        pickerModel.markCollected(orderId);
    }
}