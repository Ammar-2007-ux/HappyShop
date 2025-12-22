package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.CheckoutValidator;
import ci553.happyshop.catalogue.Order;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.exception.ExcessiveOrderQuantityException;
import ci553.happyshop.catalogue.exception.UnderMinimumPaymentException;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.ProductListFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

/**
 * TODO
 * You can either directly modify the CustomerModel class to implement the required tasks,
 * or create a subclass of CustomerModel and override specific methods where appropriate.
 */
public class CustomerModel {
    public CustomerView cusView;
    public DatabaseRW databaseRW; //Interface type, not specific implementation
                                  //Benefits: Flexibility: Easily change the database implementation.

    // Notifier windows (non-blocking) to improve customer UX.
    private final RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
    private final CheckoutFailureNotifier checkoutFailureNotifier = new CheckoutFailureNotifier();

    private Product theProduct =null; // product found from search
    private ArrayList<Product> trolley =  new ArrayList<>(); // a list of products in trolley

    // Four UI elements to be passed to CustomerView for display updates.
    private String imageName = "imageHolder.jpg";                // Image to show in product preview (Search Page)
    private String displayLaSearchResult = "No Product was searched yet"; // Label showing search result message (Search Page)
    private String displayTaTrolley = "";                                // Text area content showing current trolley items (Trolley Page)
    private String displayTaReceipt = "";                                // Text area content showing receipt after checkout (Receipt Page)

    // Tracks whether we had to cap ordered quantity during merging.
    private String lastMergeNote = "";

    //SELECT productID, description, image, unitPrice,inStock quantity
    void search() throws SQLException {
        String productId = cusView.tfId.getText().trim();
        if(!productId.isEmpty()){
            theProduct = databaseRW.searchByProductId(productId); //search database
            if(theProduct != null && theProduct.getStockQuantity()>0){
                double unitPrice = theProduct.getUnitPrice();
                String description = theProduct.getProductDescription();
                int stock = theProduct.getStockQuantity();

                String baseInfo = String.format("Product_Id: %s\n%s,\nPrice: £%.2f", productId, description, unitPrice);
                String quantityInfo = stock < 100 ? String.format("\n%d units left.", stock) : "";
                displayLaSearchResult = baseInfo + quantityInfo;
                System.out.println(displayLaSearchResult);
            }
            else{
                theProduct=null;
                displayLaSearchResult = "No Product was found with ID " + productId;
                System.out.println("No Product was found with ID " + productId);
            }
        }else{
            theProduct=null;
            displayLaSearchResult = "Please type ProductID";
            System.out.println("Please type ProductID.");
        }
        updateView();
    }

    void addToTrolley(){
        if(theProduct!= null){

            // trolley.add(theProduct) — Product is appended to the end of the trolley.
            // To keep the trolley organized, add code here or call a method that:
            //TODO
            // 1. Merges items with the same product ID (combining their quantities).
            // 2. Sorts the products in the trolley by product ID.
            // Add a copy so trolley lines are independent from the most recently searched product instance.
            trolley.add(new Product(
                    theProduct.getProductId(),
                    theProduct.getProductDescription(),
                    theProduct.getProductImageName(),
                    theProduct.getUnitPrice(),
                    theProduct.getStockQuantity()
            ));

            organiseTrolley();
            displayTaTrolley = ProductListFormatter.buildString(trolley);

            // If the merge process had to cap quantity, show a helpful message.
            if (!lastMergeNote.isBlank()) {
                displayLaSearchResult = lastMergeNote;
                lastMergeNote = "";
            }
        }
        else{
            displayLaSearchResult = "Please search for an available product before adding it to the trolley";
            System.out.println("must search and get an available product before add to trolley");
        }
        displayTaReceipt=""; // Clear receipt to switch back to trolleyPage (receipt shows only when not empty)
        updateView();
    }

    void checkOut() throws IOException, SQLException {
        if(!trolley.isEmpty()){
            // Always keep trolley organised before checkout.
            organiseTrolley();

            // Business rules validation (Week 5):
            // - Minimum total payment £5
            // - Per-item quantity maximum 50
            try {
                CheckoutValidator.validateAndGetTotal(trolley);
            } catch (UnderMinimumPaymentException | ExcessiveOrderQuantityException ex) {
                checkoutFailureNotifier.cusView = cusView;
                checkoutFailureNotifier.showMsg(ex.getMessage());
                displayLaSearchResult = ex.getMessage();
                updateView();
                return;
            }

            // Group the products in the trolley by productId to optimize stock checking
            // Check the database for sufficient stock for all products in the trolley.
            // If any products are insufficient, the update will be rolled back.
            // If all products are sufficient, the database will be updated, and insufficientProducts will be empty.
            // Note: If the trolley is already organized (merged and sorted), grouping is unnecessary.
            ArrayList<Product> groupedTrolley= new ArrayList<>(trolley); // already merged/sorted
            ArrayList<Product> insufficientProducts= databaseRW.purchaseStocks(groupedTrolley);

            if(insufficientProducts.isEmpty()){ // If stock is sufficient for all products
                //get OrderHub and tell it to make a new Order
                OrderHub orderHub =OrderHub.getOrderHub();
                Order theOrder = orderHub.newOrder(trolley);
                trolley.clear();
                displayTaTrolley ="";
                displayTaReceipt = String.format(
                        "Order_ID: %s\nOrdered_Date_Time: %s\n%s",
                        theOrder.getOrderId(),
                        theOrder.getOrderedDateTime(),
                        ProductListFormatter.buildString(theOrder.getProductList())
                );
                System.out.println(displayTaReceipt);

                // Close any old notifier windows that might still be open.
                removeProductNotifier.closeNotifierWindow();
                checkoutFailureNotifier.closeNotifierWindow();
            }
            else{ // Some products have insufficient stock — build an error message to inform the customer
                StringBuilder errorMsg = new StringBuilder();
                for(Product p : insufficientProducts){
                    errorMsg.append("\u2022 "+ p.getProductId()).append(", ")
                            .append(p.getProductDescription()).append(" (Only ")
                            .append(p.getStockQuantity()).append(" available, ")
                            .append(p.getOrderedQuantity()).append(" requested)\n");
                }
                theProduct=null;

                // 1) Remove products with insufficient stock from the trolley.
                // 2) Notify the customer via a popup window (better UX than overriding the search label).
                removeInsufficientFromTrolley(insufficientProducts);
                displayTaTrolley = trolley.isEmpty() ? "Your trolley is empty" : ProductListFormatter.buildString(trolley);

                // Ensure notifier tracks the correct main window.
                removeProductNotifier.cusView = cusView;
                removeProductNotifier.showRemovalMsg(errorMsg.toString());

                displayLaSearchResult = "Some items were removed due to low stock. Please review your trolley.";
            }
        }
        else{
            displayTaTrolley = "Your trolley is empty";
            System.out.println("Your trolley is empty");
        }
        updateView();
    }

    /**
     * Keeps trolley tidy by merging duplicate product IDs and sorting by product ID.
     * This improves readability for customers and reduces work during checkout.
     */
    private void organiseTrolley() {
        if (trolley.isEmpty()) return;

        Map<String, Product> merged = new HashMap<>();
        lastMergeNote = "";

        for (Product p : trolley) {
            String id = p.getProductId();
            if (!merged.containsKey(id)) {
                Product copy = new Product(
                        p.getProductId(),
                        p.getProductDescription(),
                        p.getProductImageName(),
                        p.getUnitPrice(),
                        p.getStockQuantity()
                );
                copy.setOrderedQuantity(p.getOrderedQuantity());
                merged.put(id, copy);
            } else {
                Product existing = merged.get(id);
                int combined = existing.getOrderedQuantity() + p.getOrderedQuantity();
                int capped = Math.min(combined, existing.getStockQuantity());
                existing.setOrderedQuantity(capped);
                if (capped < combined) {
                    lastMergeNote = "Added to trolley, but quantity was capped by current stock for product " + id + ".";
                }
            }
        }

        ArrayList<Product> organised = new ArrayList<>(merged.values());
        organised.sort(Comparator.naturalOrder());
        trolley.clear();
        trolley.addAll(organised);
    }

    private void removeInsufficientFromTrolley(ArrayList<Product> insufficient) {
        if (insufficient == null || insufficient.isEmpty()) return;
        for (Product p : insufficient) {
            trolley.removeIf(t -> t.getProductId().equals(p.getProductId()));
        }
    }

    /**
     * Groups products by their productId to optimize database queries and updates.
     * By grouping products, we can check the stock for a given `productId` once, rather than repeatedly
     */
    private ArrayList<Product> groupProductsById(ArrayList<Product> proList) {
        Map<String, Product> grouped = new HashMap<>();
        for (Product p : proList) {
            String id = p.getProductId();
            if (grouped.containsKey(id)) {
                Product existing = grouped.get(id);
                existing.setOrderedQuantity(existing.getOrderedQuantity() + p.getOrderedQuantity());
            } else {
                // Make a shallow copy to avoid modifying the original
                grouped.put(id,new Product(p.getProductId(),p.getProductDescription(),
                        p.getProductImageName(),p.getUnitPrice(),p.getStockQuantity()));
            }
        }
        return new ArrayList<>(grouped.values());
    }

    void cancel(){
        trolley.clear();
        displayTaTrolley="";

        // If customer cancels, close any notifier windows.
        removeProductNotifier.closeNotifierWindow();
        checkoutFailureNotifier.closeNotifierWindow();
        updateView();
    }
    void closeReceipt(){
        displayTaReceipt="";
    }

    void updateView() {
        if(theProduct != null){
            imageName = theProduct.getProductImageName();
            String relativeImageUrl = StorageLocation.imageFolder +imageName; //relative file path, eg images/0001.jpg
            // Get the full absolute path to the image
            Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
            imageName = imageFullPath.toUri().toString(); //get the image full Uri then convert to String
            System.out.println("Image absolute path: " + imageFullPath); // Debugging to ensure path is correct
        }
        else{
            imageName = "imageHolder.jpg";
        }
        cusView.update(imageName, displayLaSearchResult, displayTaTrolley,displayTaReceipt);
    }
     // extra notes:
     //Path.toUri(): Converts a Path object (a file or a directory path) to a URI object.
     //File.toURI(): Converts a File object (a file on the filesystem) to a URI object

    //for test only
    public ArrayList<Product> getTrolley() {
        return trolley;
    }
}
