package performance.lab3.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import performance.lab3.ProductDto;
import performance.lab3.domain.ProductCategory;
import performance.lab3.service.ProductService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(
            @PathVariable("category") ProductCategory category) {
        return ResponseEntity.ok(productService.findProductsByCategory(category));
    }

    @GetMapping("/category/{category}/average-rating")
    public ResponseEntity<Double> getAverageRatingByCategory(
            @PathVariable("category") ProductCategory category) {
        return ResponseEntity.ok(productService.calculateAverageRatingByCategory(category));
    }

    @GetMapping("/category/{category}/average-rating/caffeineCache")
    public ResponseEntity<Double> getAverageRatingUsingCaffeineCache(
            @PathVariable("category") ProductCategory category) {
        return ResponseEntity.ok(productService.calculateAverageRatingUsingCaffeineCache(category));
    }

    @GetMapping("/category/{category}/average-rating/ehcache")
    public ResponseEntity<Double> getAverageRatingUsingEhCache(
            @PathVariable("category") ProductCategory category) {
        return ResponseEntity.ok(productService.calculateAverageRatingUsingEhCache(category));
    }
}
