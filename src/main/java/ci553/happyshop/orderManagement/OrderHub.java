package ci553.happyshop.orderManagement;

import ci553.happyshop.catalogue.Order;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.client.picker.PickerModel;
import ci553.happyshop.storageAccess.OrderFileManager;
import ci553.happyshop.utility.StorageLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class OrderHub {

    private static OrderHub orderHub;

    private final Path orderedPath = StorageLocation.orderedPath;
    private final Path progressingPath = StorageLocation.progressingPath;
    private final Path collectedPath = StorageLocation.collectedPath;

    private final TreeMap<Integer, OrderState> orderMap = new TreeMap<>();

    private final ArrayList<OrderTracker> orderTrackerList = new ArrayList<>();
    private final ArrayList<PickerModel> pickerModelList = new ArrayList<>();

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    private OrderHub() {}

    public static OrderHub getOrderHub() {
        if (orderHub == null) {
            orderHub = new OrderHub();
        }
        return orderHub;
    }

    // ===================== ORDER CREATION =====================

    public Order newOrder(ArrayList<Product> trolley)
            throws IOException, SQLException {

        int orderId = OrderCounter.generateOrderId();
        String orderedDateTime =
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Order theOrder =
                new Order(orderId, OrderState.Ordered, orderedDateTime, trolley);

        OrderFileManager.createOrderFile(
                orderedPath, orderId, theOrder.orderDetails());

        orderMap.put(orderId, OrderState.Ordered);
        notifyOrderTrackers();
        notifyPickerModels();

        return theOrder;
    }

    // ===================== OBSERVERS =====================

    public void registerOrderTracker(OrderTracker tracker) {
        orderTrackerList.add(tracker);
    }

    public void registerPickerModel(PickerModel pickerModel) {
        pickerModelList.add(pickerModel);
    }

    private void notifyOrderTrackers() {
        for (OrderTracker tracker : orderTrackerList) {
            tracker.setOrderMap(new TreeMap<>(orderMap));
        }
    }

    private void notifyPickerModels() {
        TreeMap<Integer, OrderState> pickerMap = new TreeMap<>();
        for (Map.Entry<Integer, OrderState> e : orderMap.entrySet()) {
            if (e.getValue() != OrderState.Collected) {
                pickerMap.put(e.getKey(), e.getValue());
            }
        }
        for (PickerModel picker : pickerModelList) {
            picker.setOrderMap(pickerMap);
        }
    }

    // ===================== STATE CHANGE =====================

    public void changeOrderStateMoveFile(int orderId, OrderState newState)
            throws IOException {

        if (!orderMap.containsKey(orderId)) return;

        OrderState oldState = orderMap.get(orderId);
        if (oldState == newState) return;

        if (oldState == OrderState.Ordered && newState == OrderState.Progressing) {
            OrderFileManager.updateAndMoveOrderFile(
                    orderId, newState, orderedPath, progressingPath);
        }

        if (oldState == OrderState.Progressing && newState == OrderState.Collected) {
            OrderFileManager.updateAndMoveOrderFile(
                    orderId, newState, progressingPath, collectedPath);
            removeCollectedOrder(orderId);
        }

        orderMap.put(orderId, newState);
        notifyOrderTrackers();
        notifyPickerModels();
    }

    // ===================== 🔴 FIXED METHOD =====================

    /**
     * Used ONLY by Picker to read order details.
     */
    public String getOrderDetailForPicker(int orderId) throws IOException {

        OrderState state = orderMap.get(orderId);

        if (state == OrderState.Ordered) {
            return OrderFileManager.readOrderFile(orderedPath, orderId);
        }

        if (state == OrderState.Progressing) {
            return OrderFileManager.readOrderFile(progressingPath, orderId);
        }

        return "";
    }

    // ===================== INIT FROM FILES =====================

    public void initializeOrderMap() {
        loadOrdersFromDir(orderedPath, OrderState.Ordered);
        loadOrdersFromDir(progressingPath, OrderState.Progressing);
        notifyOrderTrackers();
        notifyPickerModels();
    }

    private void loadOrdersFromDir(Path dir, OrderState state) {
        if (!Files.exists(dir)) return;

        try (Stream<Path> stream = Files.list(dir)) {
            List<Path> files = stream.filter(Files::isRegularFile).toList();
            for (Path p : files) {
                String name = p.getFileName().toString();
                if (name.endsWith(".txt")) {
                    int id = Integer.parseInt(name.replace(".txt", ""));
                    orderMap.put(id, state);
                }
            }
        } catch (Exception ignored) {}
    }

    // ===================== CLEANUP =====================

    private void removeCollectedOrder(int orderId) {
        scheduler.schedule(() -> {
            orderMap.remove(orderId);
            notifyOrderTrackers();
        }, 10, TimeUnit.SECONDS);
    }
}