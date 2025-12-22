package ci553.happyshop.catalogue;

import java.util.Objects;

public class Product implements Comparable<Product> {

    private final String proId;
    private final String proDescription;
    private final String proImageName;
    private final double unitPrice;

    private int orderedQuantity = 1;
    private int stockQuantity;

    public Product(String id, String des, String image, double aPrice, int stockQuantity) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("productId required");
        if (des == null || des.isBlank()) throw new IllegalArgumentException("description required");
        if (aPrice < 0.0) throw new IllegalArgumentException("unitPrice must be >= 0");
        if (stockQuantity < 0) throw new IllegalArgumentException("stockQuantity must be >= 0");
        this.proId = id.trim();
        this.proDescription = des.trim();
        this.proImageName = (image == null || image.isBlank()) ? null : image.trim();
        this.unitPrice = aPrice;
        this.stockQuantity = stockQuantity;
        if (this.stockQuantity > 0 && this.orderedQuantity > this.stockQuantity) {
            this.orderedQuantity = this.stockQuantity;
        }
    }

    public String getProductId() { return proId; }
    public String getProductDescription() { return proDescription; }
    public String getProductImageName() { return proImageName; }
    public double getUnitPrice() { return unitPrice; }
    public int getOrderedQuantity() { return orderedQuantity; }
    public int getStockQuantity() { return stockQuantity; }

    public void setOrderedQuantity(int orderedQuantity) {
        if (orderedQuantity < 1) throw new IllegalArgumentException("orderedQuantity must be >= 1");
        if (stockQuantity > 0 && orderedQuantity > stockQuantity)
            throw new IllegalArgumentException("orderedQuantity cannot exceed stock");
        this.orderedQuantity = orderedQuantity;
    }

    public int adjustStock(int delta) {
        long candidate = (long) this.stockQuantity + delta;
        if (candidate < 0) throw new IllegalArgumentException("stock cannot be negative");
        this.stockQuantity = (int) candidate;
        if (this.stockQuantity > 0 && this.orderedQuantity > this.stockQuantity) {
            this.orderedQuantity = this.stockQuantity;
        }
        return this.stockQuantity;
    }

    @Override
    public int compareTo(Product otherProduct) {
        if (otherProduct == null) return 1;
        return this.proId.compareTo(otherProduct.proId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product other = (Product) o;
        return this.proId.equals(other.proId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proId);
    }

    @Override
    public String toString() {
        return String.format("Id: %s, £%.2f/unit, stock: %d%n%s",
                proId, unitPrice, stockQuantity, proDescription);
    }
}
