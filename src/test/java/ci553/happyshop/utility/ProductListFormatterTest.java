package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Product;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductListFormatterTest {

    @Test
    void buildString_includesTotalLineAndAmounts() {
        ArrayList<Product> products = new ArrayList<>();
        Product p = new Product("0001", "Test", "t.jpg", 2.5, 100);
        p.setOrderedQuantity(2); // £5.00
        products.add(p);

        String s = ProductListFormatter.buildString(products);
        assertTrue(s.contains("Total"));
        assertTrue(s.contains("£"));
        assertTrue(s.contains("0001"));
    }
}
