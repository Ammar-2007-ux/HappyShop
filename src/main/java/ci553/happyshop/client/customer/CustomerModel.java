package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.shared.WarehouseNotificationHub;
import ci553.happyshop.storageAccess.DatabaseRW;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerModel {

    public DatabaseRW databaseRW;
    public CustomerView cusView;

    private Product currentProduct;
    private final StringBuilder trolleyText = new StringBuilder();
    private final ArrayList<Product> trolley = new ArrayList<>();

    private static final Map<String, Product> CATALOGUE = new HashMap<>();

    static {
        CATALOGUE.put("0001", new Product("0001", "Smart TV", null, 499.99, 999));
        CATALOGUE.put("0002", new Product("0002", "Laptop", null, 899.99, 999));
        CATALOGUE.put("0003", new Product("0003", "Headphones", null, 79.99, 999));
        CATALOGUE.put("0004", new Product("0004", "Smartphone", null, 699.99, 999));
        CATALOGUE.put("0005", new Product("0005", "Toaster", null, 29.99, 999));
        CATALOGUE.put("0006", new Product("0006", "Microwave", null, 119.99, 999));
        CATALOGUE.put("0007", new Product("0007", "Blender", null, 49.99, 999));
        CATALOGUE.put("0008", new Product("0008", "Coffee Machine", null, 149.99, 999));
        CATALOGUE.put("0009", new Product("0009", "Vacuum Cleaner", null, 199.99, 999));
        CATALOGUE.put("0010", new Product("0010", "Air Fryer", null, 129.99, 999));
    }

    public void searchProduct() {
        String code = cusView.tfId.getText().trim();
        Product found = CATALOGUE.get(code);

        if (found == null) {
            currentProduct = null;
            cusView.update("imageHolder.jpg", "Product not found.", trolleyText.toString(), "");
            return;
        }

        currentProduct = found;
        cusView.update("imageHolder.jpg", found.toString(), trolleyText.toString(), "");
    }

    public void addToTrolley() {
        if (currentProduct == null) return;

        trolley.add(currentProduct);
        trolleyText.append("- ")
                .append(currentProduct.getProductDescription())
                .append("\n");

        WarehouseNotificationHub.notifyWarehouse(
                "Customer added product " + currentProduct.getProductId() + " to trolley"
        );

        cusView.update("imageHolder.jpg", currentProduct.toString(), trolleyText.toString(), "");
    }

    public void checkOut() {
        try {
            OrderHub hub = OrderHub.getOrderHub();
            hub.newOrder(new ArrayList<>(trolley)); // 🔥 CREATE ORDER

            cusView.update(
                    "imageHolder.jpg",
                    "Order placed successfully.",
                    "",
                    "Preparing order"
            );

            trolley.clear();
            trolleyText.setLength(0);
            currentProduct = null;

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}