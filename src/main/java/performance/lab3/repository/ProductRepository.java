package performance.lab3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import performance.lab3.domain.Product;
import performance.lab3.domain.ProductCategory;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(ProductCategory category);

    @Query("SELECT AVG(r.rating) FROM Product p JOIN p.reviews r WHERE p.category = :category GROUP BY p.category")
    Double calculateAverageRatingByCategory(@Param("category") ProductCategory category);
}
