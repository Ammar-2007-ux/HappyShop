package ci553.happyshop.client.picker;

import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.orderManagement.OrderState;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * PickerModel handles orders that are being prepared.
 * It is the ONLY class allowed to read order details.
 */
public class PickerModel {

    private final OrderHub orderHub = OrderHub.getOrderHub();

    // orderId -> state (Ordered / Progressing)
    private final TreeMap<Integer, OrderState> orderMap = new TreeMap<>();

    // currently selected order
    private int selectedOrderId = -1;

    // text shown in picker UI
    public String displayTaOrderDetail = "";

    /**
     * Called by OrderHub when order states change
     */
    public void setOrderMap(TreeMap<Integer, OrderState> om) {
        orderMap.clear();
        orderMap.putAll(om);
    }

    /**
     * Called when picker selects an order in the UI
     */
    public void selectOrder(int orderId) {
        selectedOrderId = orderId;
        try {
            displayTaOrderDetail = orderHub.getOrderDetailForPicker(orderId);
        } catch (IOException e) {
            displayTaOrderDetail = "Error loading order details";
        }
    }

    /**
     * Used by PickerController
     */
    public int getSelectedOrderId() {
        return selectedOrderId;
    }

    /**
     * Picker marks order as progressing
     */
    public void startPreparing(int orderId) {
        try {
            orderHub.changeOrderStateMoveFile(orderId, OrderState.Progressing);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Picker marks order as collected
     */
    public void markCollected(int orderId) {
        try {
            orderHub.changeOrderStateMoveFile(orderId, OrderState.Collected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used by picker UI to list orders
     */
    public Map<Integer, OrderState> getOrderMap() {
        return orderMap;
    }
}