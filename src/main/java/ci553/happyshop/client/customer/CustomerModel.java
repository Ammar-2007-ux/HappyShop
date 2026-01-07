package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.storageAccess.DatabaseRW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerModel {

    // kept for compatibility with existing wiring
    public DatabaseRW databaseRW;

    public CustomerView cusView;

    private Product currentProduct;

    // holds readable trolley text
    private final StringBuilder trolleyText = new StringBuilder();

    // ✅ holds actual products for price calculation
    private final ArrayList<Product> trolley = new ArrayList<>();

    // hardcoded catalogue (same idea as before)
    private static final Map<String, Product> CATALOGUE = new HashMap<>();

    static {
        CATALOGUE.put("0001", new Product("0001", "Smart TV", null, 499.99, 20));
        CATALOGUE.put("0002", new Product("0002", "Laptop", null, 899.99, 15));
        CATALOGUE.put("0003", new Product("0003", "Headphones", null, 79.99, 50));
        CATALOGUE.put("0004", new Product("0004", "Smartphone", null, 699.99, 30));
        CATALOGUE.put("0005", new Product("0005", "Toaster", null, 29.99, 40));
        CATALOGUE.put("0006", new Product("0006", "Microwave", null, 119.99, 25));
        CATALOGUE.put("0007", new Product("0007", "Blender", null, 49.99, 35));
        CATALOGUE.put("0008", new Product("0008", "Coffee Machine", null, 149.99, 18));
        CATALOGUE.put("0009", new Product("0009", "Vacuum Cleaner", null, 199.99, 22));
        CATALOGUE.put("0010", new Product("0010", "Air Fryer", null, 129.99, 28));
    }

    // 🔍 Search product
    public void searchProduct() {

        String code = cusView.tfId.getText();
        if (code == null) code = "";
        code = code.trim();

        Product found = CATALOGUE.get(code);

        if (found == null) {
            currentProduct = null;
            cusView.update(
                    "imageHolder.jpg",
                    "Product not found.",
                    trolleyText.toString(),
                    ""
            );
            return;
        }

        currentProduct = found;

        cusView.update(
                "imageHolder.jpg",
                "Product ID: " + found.getProductId() + "\n" +
                        "Product: " + found.getProductDescription() + "\n" +
                        "Price: £" + found.getUnitPrice(),
                trolleyText.toString(),
                ""
        );
    }

    // 🛒 Add to trolley
    public void addToTrolley() {

        if (currentProduct == null) return;

        trolley.add(currentProduct);

        trolleyText.append("- ")
                .append(currentProduct.getProductDescription())
                .append(" (£")
                .append(currentProduct.getUnitPrice())
                .append(")\n");

        cusView.update(
                "imageHolder.jpg",
                currentProduct.toString(),
                trolleyText.toString(),
                ""
        );
    }

    // 🧾 Checkout + total price
    public void checkOut() {

        double total = 0.0;
        for (Product p : trolley) {
            total += p.getUnitPrice();
        }

        cusView.update(
                "imageHolder.jpg",
                "Checkout successful.\n\n" +
                        "Total price: £" + String.format("%.2f", total),
                "",
                "Thank you for shopping with HappyShop!"
        );

        // clear everything
        trolley.clear();
        trolleyText.setLength(0);
        currentProduct = null;
    }
}