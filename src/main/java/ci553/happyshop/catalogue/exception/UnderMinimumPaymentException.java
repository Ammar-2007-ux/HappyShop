package ci553.happyshop.catalogue.exception;

/**
 * Thrown when a customer attempts to check out with a trolley total below the business minimum.
 */
public class UnderMinimumPaymentException extends Exception {
    public UnderMinimumPaymentException(double total) {
        super(String.format("Minimum payment is £5.00. Your current total is £%.2f.", total));
    }
}
