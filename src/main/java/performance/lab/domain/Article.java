package performance.lab.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Article create(Long articleId, String content, User user){
        Article article = new Article();
        article.articleId = articleId;
        article.content = content;
        article.user = user;
        return article;
    }
}
