package performance.lab.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_title_like", columnList = "title,like_count"),
        @Index(name = "idx_reverse_title_like", columnList = "like_count,title")
})
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String title;
    private String content;
    private Integer likeCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Article create(Long articleId, String title, String content, Integer likeCount, User user) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.likeCount = likeCount;
        article.user = user;
        return article;
    }
}
