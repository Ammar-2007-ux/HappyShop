package ci553.happyshop.catalogue;

import ci553.happyshop.catalogue.exception.ExcessiveOrderQuantityException;
import ci553.happyshop.catalogue.exception.UnderMinimumPaymentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutValidatorTest {

    @Test
    void validateAndGetTotal_throwsUnderMinimumPayment_whenTotalBelowFive() {
        Product p = new Product("0001", "A", "a.jpg", 1.0, 100);
        p.setOrderedQuantity(4); // £4.00
        assertThrows(UnderMinimumPaymentException.class,
                () -> CheckoutValidator.validateAndGetTotal(List.of(p)));
    }

    @Test
    void validateAndGetTotal_throwsExcessiveQuantity_whenAnyLineExceedsMax() {
        Product p = new Product("0001", "A", "a.jpg", 0.2, 1000);
        p.setOrderedQuantity(51);
        assertThrows(ExcessiveOrderQuantityException.class,
                () -> CheckoutValidator.validateAndGetTotal(List.of(p)));
    }

    @Test
    void validateAndGetTotal_returnsTotal_whenValid() throws Exception {
        Product p1 = new Product("0001", "A", "a.jpg", 2.0, 100);
        p1.setOrderedQuantity(2); // £4
        Product p2 = new Product("0002", "B", "b.jpg", 1.5, 100);
        p2.setOrderedQuantity(1); // £1.5

        double total = CheckoutValidator.validateAndGetTotal(List.of(p1, p2));
        assertEquals(5.5, total, 0.0001);
    }
}
