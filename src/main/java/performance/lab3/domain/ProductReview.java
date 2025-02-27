package performance.lab3.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String author;
    private String content;
    private int rating; // 1-5 평점

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean verified; // 구매 검증 여부

    public ProductReview(Product product, String author, String content, int rating) {
        this.product = product;
        this.author = author;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
        this.verified = false;
    }

    public void markAsVerified() {
        this.verified = true;
    }
}
