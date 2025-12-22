package ci553.happyshop.catalogue.exception;

/**
 * Thrown when a single product line exceeds the allowed maximum order quantity.
 */
public class ExcessiveOrderQuantityException extends Exception {
    public ExcessiveOrderQuantityException(String productId, int qty) {
        super(String.format("Product %s exceeds the maximum allowed quantity (50). Requested: %d.",
                productId, qty));
    }
}
