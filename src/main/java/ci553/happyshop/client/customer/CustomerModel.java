package ci553.happyshop.client.customer;

import ci553.happyshop.storageAccess.DatabaseRW;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * CustomerModel handles customer-side logic only.
 * Images are shown during search, trolley displays text only.
 */
public class CustomerModel {

    // Required by starter wiring
    public CustomerView cusView;
    public DatabaseRW databaseRW;

    // Keeps track of the last searched product
    private String currentCode;
    private String currentDescription;

    // Product code → description mapping
    private static final Map<String, String> PRODUCT_DESCRIPTIONS = new HashMap<>();

    static {
        PRODUCT_DESCRIPTIONS.put("0001", "Television");
        PRODUCT_DESCRIPTIONS.put("0002", "Radio");
        PRODUCT_DESCRIPTIONS.put("0003", "Toaster");
        PRODUCT_DESCRIPTIONS.put("0004", "Bottled Water");
        PRODUCT_DESCRIPTIONS.put("0005", "Digital Camera");
        PRODUCT_DESCRIPTIONS.put("0006", "MP3 Player");
        PRODUCT_DESCRIPTIONS.put("0007", "USB Flash Drive");
        PRODUCT_DESCRIPTIONS.put("0008", "USB Flash Drive");
        PRODUCT_DESCRIPTIONS.put("0009", "USB Flash Drive");
        PRODUCT_DESCRIPTIONS.put("0010", "USB Flash Drive");
    }

    // Stores trolley text only (no images)
    private final StringBuilder trolleyText = new StringBuilder();

    public CustomerModel() {
    }

    /**
     * Searches for a product by code and displays image + description.
     */
    public void searchProduct() throws SQLException, IOException {

        String code = cusView.tfId.getText().trim();

        if (!PRODUCT_DESCRIPTIONS.containsKey(code)) {
            cusView.update(
                    "imageHolder.jpg",
                    "Product not found.",
                    trolleyText.toString(),
                    ""
            );
            return;
        }

        currentCode = code;
        currentDescription = PRODUCT_DESCRIPTIONS.get(code);

        cusView.update(
                code + ".jpg",                    // show product image
                currentDescription,               // show description
                trolleyText.toString(),
                ""
        );
    }

    /**
     * Adds product description to trolley (text only).
     */
    public void addToTrolley() throws SQLException, IOException {

        if (currentDescription == null) return;

        trolleyText.append("- ").append(currentDescription).append("\n");

        cusView.update(
                "imageHolder.jpg",                // no image update
                "Item added to trolley.",
                trolleyText.toString(),            // text only
                ""
        );
    }

    /**
     * Clears the trolley.
     */
    public void cancelTrolley() throws SQLException, IOException {

        trolleyText.setLength(0);

        cusView.update(
                "imageHolder.jpg",
                "Trolley cleared.",
                "",
                ""
        );
    }

    /**
     * Checkout action.
     */
    public void checkOut() throws SQLException, IOException {

        cusView.update(
                "imageHolder.jpg",
                "Checkout successful.",
                "",
                "Thank you for your purchase!"
        );

        trolleyText.setLength(0);
        currentCode = null;
        currentDescription = null;
    }
}
