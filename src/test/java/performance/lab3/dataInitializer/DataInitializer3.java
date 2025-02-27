package performance.lab3.dataInitializer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import performance.lab3.LabApplication3;
import performance.lab3.domain.Product;
import performance.lab3.domain.ProductCategory;
import performance.lab3.domain.ProductReview;

@SpringBootTest(classes = LabApplication3.class)
@ActiveProfiles("test3")
public class DataInitializer3 {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    // 설정값
    static final int PRODUCTS_PER_BATCH = 500;
    static final int REVIEWS_PER_PRODUCT_MIN = 15;
    static final int REVIEWS_PER_PRODUCT_MAX = 30;
    static final int EXECUTE_COUNT = 20; // 20개 배치 = 500 * 20 = 10,000개 상품

    // 랜덤 데이터 생성용
    private final Random random = new Random();
    private final String[] reviewAuthors = {"사용자1", "구매자2", "소비자3", "테스터4", "평가자5", "리뷰어6", "고객7", "소비자8", "VIP고객9",
            "단골10"};
    private final String[] reviewContents = {
            "아주 만족스러운 제품입니다. 배송도 빠르고 포장도 꼼꼼했어요.",
            "가성비 좋은 제품입니다. 다음에도 구매할 것 같아요.",
            "기대했던 것보다는 조금 아쉬웠어요. 하지만 가격 대비 괜찮습니다.",
            "품질이 정말 좋습니다. 오래 사용할 수 있을 것 같아요.",
            "디자인이 예쁘고 기능도 좋습니다. 추천합니다!",
            "사용하기 편리하고 성능도 좋습니다. 만족스러운 구매였어요.",
            "배송이 조금 늦었지만 제품 자체는 좋습니다.",
            "가격 대비 최고의 선택이었습니다. 다른 제품보다 훨씬 좋아요.",
            "재구매의사 있습니다. 품질이 좋고 내구성도 뛰어나요.",
            "친구들에게도 추천했어요. 모두 만족하고 있습니다."
    };
    private final String[] productDescTemplates = {
            "고품질 {0} 제품입니다. 내구성이 뛰어나고 사용감이 좋습니다.",
            "최신 기술이 적용된 {0}입니다. 혁신적인 디자인과 기능을 갖추고 있습니다.",
            "가성비 좋은 {0} 제품으로, 합리적인 가격에 좋은 품질을 제공합니다.",
            "프리미엄 {0}으로 최상의 경험을 선사합니다.",
            "다용도로 활용 가능한 {0} 제품입니다. 다양한 상황에서 활용해보세요."
    };

    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            final int batchIndex = i;
            executorService.submit(() -> {
                try {
                    insertBatch(batchIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                    System.out.println("Remaining batches: " + latch.getCount());
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // 대략적인 통계 출력
        System.out.println("데이터 생성 완료!");
        int totalProducts = PRODUCTS_PER_BATCH * EXECUTE_COUNT;
        int avgReviewsPerProduct = (REVIEWS_PER_PRODUCT_MIN + REVIEWS_PER_PRODUCT_MAX) / 2;
        int estimatedTotalReviews = totalProducts * avgReviewsPerProduct;
        System.out.println("생성된 상품 수 (약): " + totalProducts);
        System.out.println("생성된 리뷰 수 (약): " + estimatedTotalReviews);
    }

    void insertBatch(int batchIndex) {
        transactionTemplate.executeWithoutResult(status -> {
            ProductCategory[] categories = ProductCategory.values();

            for (int i = 0; i < PRODUCTS_PER_BATCH; i++) {
                // 상품 생성
                ProductCategory category = categories[random.nextInt(categories.length)];
                String productName = category.name() + "_Product_" + batchIndex + "_" + i;
                String description = getRandomDescription(category.name().toLowerCase());

                BigDecimal price = BigDecimal.valueOf(10 + random.nextInt(990)); // 10~999 범위 가격
                int stockQuantity = 10 + random.nextInt(991); // 10~1000 범위 재고

                Product product = new Product(
                        productName,
                        description,
                        price,
                        stockQuantity,
                        category
                );

                entityManager.persist(product);

                // 상품당 리뷰 생성
                int reviewCount = REVIEWS_PER_PRODUCT_MIN +
                        random.nextInt(REVIEWS_PER_PRODUCT_MAX - REVIEWS_PER_PRODUCT_MIN + 1);

                for (int j = 0; j < reviewCount; j++) {
                    String author = reviewAuthors[random.nextInt(reviewAuthors.length)];
                    String content = reviewContents[random.nextInt(reviewContents.length)];
                    int rating = 1 + random.nextInt(5); // 1~5 평점

                    ProductReview review = new ProductReview(
                            product,
                            author,
                            content,
                            rating
                    );

                    // 30% 확률로 인증 구매 표시
                    if (random.nextInt(100) < 30) {
                        review.markAsVerified();
                    }

                    entityManager.persist(review);
                    product.addReview(review);
                }

                // 메모리 관리를 위해 일정 주기로 flush 및 clear
                if (i % 10 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                    System.out.println("Batch " + batchIndex + ": " + (i + 1) + " products processed");
                }
            }
        });
    }

    // 카테고리에 맞는 랜덤 설명 생성
    private String getRandomDescription(String categoryName) {
        String template = productDescTemplates[random.nextInt(productDescTemplates.length)];
        return template.replace("{0}", categoryName);
    }
}
