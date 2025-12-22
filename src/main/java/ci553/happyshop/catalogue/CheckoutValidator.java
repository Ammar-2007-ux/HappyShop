package ci553.happyshop.catalogue;

import ci553.happyshop.catalogue.exception.ExcessiveOrderQuantityException;
import ci553.happyshop.catalogue.exception.UnderMinimumPaymentException;

import java.util.List;

/**
 * Single-responsibility validator for checkout business rules.
 *
 * Keeping this logic out of UI code makes it easier to test and reuse.
 */
public final class CheckoutValidator {
    public static final double MIN_PAYMENT = 5.00;
    public static final int MAX_QTY_PER_PRODUCT = 50;

    private CheckoutValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Validates the products about to be checked out.
     *
     * @param products trolley lines (ideally already merged by product ID)
     * @return the computed trolley total
     */
    public static double validateAndGetTotal(List<Product> products)
            throws UnderMinimumPaymentException, ExcessiveOrderQuantityException {
        double total = 0.0;
        for (Product p : products) {
            int qty = p.getOrderedQuantity();
            if (qty > MAX_QTY_PER_PRODUCT) {
                throw new ExcessiveOrderQuantityException(p.getProductId(), qty);
            }
            total += p.getUnitPrice() * qty;
        }
        if (total < MIN_PAYMENT) {
            throw new UnderMinimumPaymentException(total);
        }
        return total;
    }
}
