package performance.lab.dto;

import performance.lab.domain.Article;

public record ArticleResponse(
        Long articleId,
        String content
) {
    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getArticleId(),
                article.getContent()
        );
    }
}
