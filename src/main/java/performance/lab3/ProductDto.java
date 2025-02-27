package performance.lab3;

import java.math.BigDecimal;
import performance.lab3.domain.Product;
import performance.lab3.domain.ProductCategory;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stockQuantity,
        ProductCategory category
) {
    public static ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory()
        );
    }
}
