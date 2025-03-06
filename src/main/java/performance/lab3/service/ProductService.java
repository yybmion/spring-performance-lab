package performance.lab3.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import performance.lab3.ProductDto;
import performance.lab3.domain.Product;
import performance.lab3.domain.ProductCategory;
import performance.lab3.monitor.LogExecutionTime;
import performance.lab3.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * 카테고리별 상품 조회 (기본 기능)
     */
    @Transactional(readOnly = true)
    public List<ProductDto> findProductsByCategory(ProductCategory category) {
        log.info("카테고리별 상품 조회: {}", category);
        long startTime = System.currentTimeMillis();

        List<Product> products = productRepository.findByCategory(category);
        List<ProductDto> result = products.stream()
                .map(ProductDto::convertToDto)
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        log.info("카테고리별 상품 조회 완료: {}ms, 조회된 상품 수: {}", (endTime - startTime), products.size());

        return result;
    }

    /**
     * 집계 함수: 카테고리별 평균 평점 계산 - 캐싱 테스트 대상
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public Double calculateAverageRatingByCategory(ProductCategory category) {
        log.info("카테고리별 평균 평점 계산: {}", category);
        long startTime = System.currentTimeMillis();

        Double averageRating = productRepository.calculateAverageRatingByCategory(category);

        long endTime = System.currentTimeMillis();
        log.info("카테고리별 평균 평점 계산 완료: {}ms, 결과: {}", (endTime - startTime), averageRating);

        return averageRating != null ? averageRating : 0.0;
    }

    /**
     * 캐싱을 적용한 카테고리별 평균 평점 계산
     */
    @Cacheable(value = "averageRatingByCategory", key = "#p0")
    @Transactional(readOnly = true)
    public Double calculateAverageRatingUsingCaffeineCache(ProductCategory category) {
        log.info("캐시 미스! 카테고리별 평균 평점 계산: {}", category);
        long startTime = System.currentTimeMillis();

        Double averageRating = productRepository.calculateAverageRatingByCategory(category);

        long endTime = System.currentTimeMillis();
        log.info("카테고리별 평균 평점 계산 완료: {}ms, 결과: {}", (endTime - startTime), averageRating);

        return averageRating != null ? averageRating : 0.0;
    }

    /**
     * EhCache를 사용한 카테고리별 평균 평점 계산
     */
    @Cacheable(value = "averageRatingByCategory_ehcache", key = "#p0", cacheManager = "ehCacheCacheManager")
    @Transactional(readOnly = true)
    public Double calculateAverageRatingUsingEhCache(ProductCategory category) {
        log.info("EhCache 미스! 카테고리별 평균 평점 계산: {}", category);
        long startTime = System.currentTimeMillis();

        Double averageRating = productRepository.calculateAverageRatingByCategory(category);

        long endTime = System.currentTimeMillis();
        log.info("EhCache - 카테고리별 평균 평점 계산 완료: {}ms, 결과: {}", (endTime - startTime), averageRating);

        return averageRating != null ? averageRating : 0.0;
    }
}
