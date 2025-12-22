package ci553.happyshop.catalogue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void compareTo_sortsByProductId() {
        Product a = new Product("0001", "A", "a.jpg", 1.0, 10);
        Product b = new Product("0002", "B", "b.jpg", 1.0, 10);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    @Test
    void setOrderedQuantity_rejectsZeroOrNegative() {
        Product p = new Product("0001", "A", "a.jpg", 1.0, 10);
        assertThrows(IllegalArgumentException.class, () -> p.setOrderedQuantity(0));
        assertThrows(IllegalArgumentException.class, () -> p.setOrderedQuantity(-1));
    }

    @Test
    void setOrderedQuantity_cannotExceedStock() {
        Product p = new Product("0001", "A", "a.jpg", 1.0, 3);
        assertThrows(IllegalArgumentException.class, () -> p.setOrderedQuantity(4));
        p.setOrderedQuantity(3);
        assertEquals(3, p.getOrderedQuantity());
    }
}
